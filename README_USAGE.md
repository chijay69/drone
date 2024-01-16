# Musala Drone Management System

This is a simple Drone Management System designed using Spring Boot, JPA/Hibernate, and H2 Database. The system allows users to register drones, load medication items onto drones, check battery levels, and retrieve information about loaded medications.

## Project Structure

- **com.example.musala.controller**: Contains the REST controllers for handling HTTP requests.
- **com.example.musala.converter**: Converts between DTOs (Data Transfer Objects) and entity classes.
- **com.example.musala.data.dto**: Data Transfer Objects used for input and output.
- **com.example.musala.data.enums**: Enumerations used in the project.
- **com.example.musala.data.model**: Entity classes representing the data model.
- **com.example.musala.repository**: JPA repositories for interacting with the database.
- **com.example.musala.service**: Service interfaces and implementations for business logic.
- **com.example.musala.utils**: Utilities class and implementations for Drone Battery logging logic.

## Endpoints

- **POST /drones/register**: Register a new drone.
- **POST /drones/{serialNumber}/load**: Load medication items onto a drone.
- **GET /drones/{serialNumber}/battery-level**: Check the battery level of a drone.
- **GET /drones/available-for-loading**: Get a list of available drones for loading.
- **GET /drones/{serialNumber}/loaded-medications**: Get a list of medications loaded onto a drone.

## Usage

1. **Drone Registration**: Use the `/drones/register` endpoint with a POST request to register a new drone. Provide the necessary information in the request body.

    ```json
    {
      "serialNumber": "63416A8D84ACC705C8F8FA",
      "model": "Middleweight",
      "weightLimit": 250.0,
      "batteryCapacity": 80,
      "state": "IDLE",
      "loadedMedications": []
    }
    ```

2. **Load Medication Items**: Use the `/drones/{serialNumber}/load` endpoint with a POST request to load medication items onto a drone. Provide the serial number of the drone and a list of medication request DTOs in the request body.

    ```json
    [
      {
        "weight": 10.0,
        "name": "Medication1",
        "image": null
      },
      {
        "weight": 15.0,
        "name": "Medication2",
        "image": null
      }
    ]
    ```

3. **Check Drone Battery Level**: Use the `/drones/{serialNumber}/battery-level` endpoint with a GET request to check the battery level of a drone.

4. **Get Available Drones for Loading**: Use the `/drones/available-for-loading` endpoint with a GET request to get a list of drones available for loading.

5. **Get Loaded Medications for a Drone**: Use the `/drones/{serialNumber}/loaded-medications` endpoint with a GET request to get a list of medications loaded onto a drone.

## Running the Application

1. Clone the repository:

    ```bash
    git clone https://github.com/your-username/musala-drone-management.git
    ```

2. Open the project in your preferred IDE (e.g., IntelliJ, Eclipse).

3. Run the `MusalaDroneManagementApplication` class to start the Spring Boot application.

4. Use a tool like Postman to interact with the provided endpoints.

**Note**: The application uses an H2 in-memory database. You can access the H2 console at `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:file:./src/main/resources/testDB`. The console provides insights into the database and allows you to execute SQL queries.

Feel free to modify and extend the project based on your requirements.