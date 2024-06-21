# Kelana

Kelana is a mobile application designed to help budget-conscious travelers plan their trips efficiently by providing real-time pricing information for attractions, accommodations, and transportation. The app leverages AI to offer personalized tour bundles, optimizing both cost and experience.

## Features
- **Real-time Pricing Information**: Get the latest prices for attractions, accommodations, and transportation.
- **Personalized Tour Bundles**: AI-driven recommendations based on user preferences and budget.
- **User-friendly Design**: An intuitive and aesthetically pleasing interface.
- **Google Maps Integration**: Display maps and navigate to destinations easily.
- **API Integration**: Communicate with external APIs using Retrofit and parse JSON data with GSON.

## Tech Stack
## Android
- **Language**: Kotlin
- **IDE**: Android Studio
- **APIs**: Google Maps API, Custom Travel API
- **Libraries**: Retrofit, GSON
- **Design**: Figma

## Machine Learning
- **Language**: Python 3.8 or higher
- **IDE**: Visual Studio Code/Pycharm/Jupyter Notebook or similiar IDE
- **APIs**: Google Maps API
- **Libraries**: numpy, pandas, googlemaps, ortools, tensorflow, tokenizer

## Getting Started

### Prerequisites
#### Android
- Android Studio installed on your machine
- Google Maps API Key
- Basic knowledge of Kotlin and Android development
#### Machine Learning
- Installed Python Intepreter
- Installed Python supported IDE
- Python intermediate proficiency
- Familiar with numpy and pandas library
- Basic Knowlegde about Natural Language Processing and Embedding Vector

### Machine Learning
#### Set up environment
- create folder for the project, then setting up the virtual environment (VS Code, Pycharm, etc) or setting the kernel (Jupyter Notebook)
  ```
  python -m venv venv
  .\venv\Scripts\activate
  ```
- download requirements.txt, placed in same folder of the project
- install all requirements by run this command
  ```
  pip install -r requirements.txt
  ```
#### Data Preparation
 - download indonesia_tourism.xlsx as main dataset
 - download review_scraping.ipynd to scrap review
 - download distance_duration_matrix.ipynb to scrap distance between destination
 - download hotel_scrap.ipynb to scrap hotel data

### Data Scraping
- run all the ipynb file, if prefer to .py file then copy it one by one to .py file, or upload .ipynb to Google Colab, then download as .py file
- (optional) rename the file if wanted by changing the file name in this similar line of code
  ```
  df_duration.to_excel("excel-file-name.xlsx", sheet_name='Duration Matrix', index=False)
  ```
 - or preferably using .csv file
  ```
  df_duration.to_csv("excel-file-name.csv", sheet_name='Duration Matrix', index=False)
  ```
### Similarity Prediction
- download similarity_destination.ipynb
- run all the code
- the result will be excel with similarity score for each destination
### Trip Planner
- download trip_planner.ipynb
- run all the code
- the result will be JSON contain each day trip plan
