# Real-Time Event Ticketing System

## Description
The Real-Time Event Ticketing System is a CLI-based project designed to manage and facilitate the release and retrieval of event tickets in real-time using multi-threaded vendor and customer handling concepts.

## Features
- Configurable ticketing system
- Multi-threaded vendor and customer handling
- Real-time ticket release and retrieval

## Configuration
The configuration is stored in a `configuration.json` file. The configuration includes:
- Total number of tickets
- Ticket release rate (tickets per interval)
- Customer retrieval rate (customers per interval)
- Maximum ticket capacity

## Classes
### Main
The entry point of the application. It handles the configuration loading, thread initialization for vendors and customers, and displays the menu.

### Configuration
Handles loading and saving the configuration from/to a JSON file. It also provides methods to input new configuration values.

### Ticket
Represents a ticket with an ID.

### TicketPool
Manages the pool of tickets, including adding and retrieving tickets.

### Vendor
Represents a vendor that releases tickets into the pool.

### Customer
Represents a customer that retrieves tickets from the pool.

## Usage
1. Run the `Main` class.
2. If a previous configuration exists, choose to use it or enter a new configuration.
3. The system will start with the configured number of vendors and customers.
4. Use the menu to exit the application.

## Dependencies
- Gson library for JSON handling

## How to Run
1. Ensure you have Java installed.
2. Compile the project using your preferred IDE or command line.
3. Run the `Main` class.

## Example Configuration (`configuration.json`)
```json
{
  "totalTickets": 20,
  "ticketReleaseRate": 5,
  "customerRetrievalRate": 2,
  "maxTicketCapacity": 20
}
