import pandas as pd
import json
from flask import Flask, request, jsonify
from ortools.constraint_solver import routing_enums_pb2
from ortools.constraint_solver import pywrapcp

app = Flask(__name__)

def read_distance_matrix(city):
    destination_filepath = f'distance_matrices/{city.lower()}_distance_duration_matrices.xlsx'
    df = pd.read_excel(destination_filepath, sheet_name='Distance Matrix')
    df.columns = ['Source', 'Destination', 'Distance (meters)']
    df['Type'] = 'Tourist Attraction'
    
    unique_destinations = pd.unique(df[['Source', 'Destination']].values.ravel('K'))
    destination_index = {destination: index for index, destination in enumerate(unique_destinations)}
    
    distance_matrix = [[0] * len(unique_destinations) for _ in range(len(unique_destinations))]
    
    for _, row in df.iterrows():
        source_index = destination_index[row['Source']]
        dest_index = destination_index[row['Destination']]
        distance_matrix[source_index][dest_index] = row['Distance (meters)']
    
    return distance_matrix, destination_index, unique_destinations

def read_destination_details(city):
    df = pd.read_excel("capstone_dataset.xlsx", sheet_name="indonesia_tourism")
    prices = df['Price'].tolist()
    times = df['Time_Minutes'].tolist()
    categories = df['Category'].tolist()
    place_names = df['Place_Name'].tolist()
    return prices, times, categories, place_names

def create_data_model(city, price, destination_limit_per_day, num_days, preferences, num_vehicles=1, depot=0):
    distance_matrix, destination_index, unique_destinations = read_distance_matrix(city)
    prices, times, categories, place_names = read_destination_details(city)
    
    data = {}
    data['distance_matrix'] = distance_matrix
    data['prices'] = prices
    data['average_spent_time'] = times
    data['categories'] = categories
    data['place_names'] = place_names
    data['price_threshold'] = price
    data['destination_limit_per_day'] = destination_limit_per_day
    data['num_days'] = num_days
    data['num_vehicles'] = num_vehicles
    data['depot'] = depot
    data['destination_index'] = destination_index
    data['city'] = city
    data['preferences'] = preferences
    
    return data

def select_hotel_within_budget(city, remaining_budget, hotels):
    valid_hotels = hotels[hotels['price'] <= remaining_budget]
    if valid_hotels.empty:
        return None
    return valid_hotels.loc[valid_hotels['price'].idxmin(), 'name']

def prioritize_destinations(data):
    preferences = data['preferences']
    preferred_destinations = []
    other_destinations = []
    
    for idx, category in enumerate(data['categories']):
        if category in preferences:
            preferred_destinations.append(idx)
        else:
            other_destinations.append(idx)
    
    return preferred_destinations, other_destinations

def solve_routing(data):
    manager = pywrapcp.RoutingIndexManager(len(data['distance_matrix']), data['num_vehicles'], data['depot'])
    routing = pywrapcp.RoutingModel(manager)
    
    def distance_callback(from_index, to_index):
        from_node = manager.IndexToNode(from_index)
        to_node = manager.IndexToNode(to_index)
        return data['distance_matrix'][from_node][to_node]
    
    transit_callback_index = routing.RegisterTransitCallback(distance_callback)
    routing.SetArcCostEvaluatorOfAllVehicles(transit_callback_index)
    
    search_parameters = pywrapcp.DefaultRoutingSearchParameters()
    search_parameters.first_solution_strategy = (routing_enums_pb2.FirstSolutionStrategy.PATH_CHEAPEST_ARC)
    
    solution = routing.SolveWithParameters(search_parameters)
    
    return manager, routing, solution

def print_solution(manager, routing, solution, data):
    preferred_destinations, other_destinations = prioritize_destinations(data)
    
    total_route = []
    total_distance = 0
    total_price = 0
    
    for vehicle_id in range(data['num_vehicles']):
        index = routing.Start(vehicle_id)
        while not routing.IsEnd(index):
            node_index = manager.IndexToNode(index)
            total_route.append(node_index)
            index = solution.Value(routing.NextVar(index))
        total_route.append(manager.IndexToNode(index))  # Add the depot/end point
    
    num_destinations = len(total_route) - 1  # Excluding the depot
    stops_per_day = data['destination_limit_per_day']
    
    remaining_budget = data['price_threshold']
    hotels = pd.read_excel(f'hotel/top_{data["city"].lower()}_hotel.xlsx')
    start_hotel = select_hotel_within_budget(data["city"].lower(), remaining_budget, hotels)
    
    all_days_data = []

    for day in range(data['num_days']):
        start_index = day * stops_per_day
        end_index = min(start_index + stops_per_day, num_destinations)
        day_route = total_route[start_index:end_index]
        
        if len(day_route) == 0:
            break  # No more destinations to assign
        
        day_data = {
            "day": day + 1,
            "starting_hotel": start_hotel,
            "stops": [],
            "ending_hotel": start_hotel
        }
        
        day_distance = 0
        day_price = 0
        
        for i in range(len(day_route) - 1):
            from_node = day_route[i]
            to_node = day_route[i + 1]
            destination_name = data['place_names'][from_node]
            destination_price = data['prices'][from_node]
            
            arc_distance = data['distance_matrix'][from_node][to_node]
            day_distance += arc_distance
            day_price += destination_price
            
            day_data["stops"].append({
                "name": destination_name,
                "price": destination_price,
                "distance": arc_distance,
                "category": data['categories'][from_node]
            })
        
        day_distance += data['distance_matrix'][day_route[-1]][0]  # Round trip to depot
        
        day_data["total_distance"] = day_distance
        day_data["total_price"] = day_price
        
        all_days_data.append(day_data)
        
        total_distance += day_distance
        total_price += day_price
    
    final_result = {
        "total_distance": total_distance,
        "total_price": total_price,
        "routes": all_days_data
    }

    return final_result

@app.route('/plan_trip', methods=['POST'])
def plan_trip():
    data = request.json
    city = data.get('city')
    price_threshold = data.get('price_threshold')
    destination_limit_per_day = data.get('destination_limit_per_day')
    num_days = data.get('num_days')
    preferences = data.get('preferences')
    
    model_data = create_data_model(city, price_threshold, destination_limit_per_day, num_days, preferences)
    manager, routing, solution = solve_routing(model_data)
    
    if solution:
        result = print_solution(manager, routing, solution, model_data)
        return jsonify(result)
    else:
        return jsonify({"error": "No solution found!"}), 400

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8080)
