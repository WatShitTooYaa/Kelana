# Use the official Node.js image
FROM node:22.3.0

# Create and change to the app directory
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install app dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 3000

# Define environment variable
ENV PORT=3000

# Run the application
CMD ["npm", "start"]
