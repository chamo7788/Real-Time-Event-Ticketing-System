import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate; // tickets per interval
    private int customerRetrievalRate; // customers per interval
    private int maxTicketCapacity;

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void loadConfiguration(){
        Scanner input = new Scanner(System.in);
        System.out.println("*** Real-Time Event Ticketing System Configuration ***");
        saveConfigurationToFile("configuration.json");
        // Total Number of Tickets
        while (true){
            System.out.println("Enter Total Number of Tickets");
            String userInput = input.nextLine();
            try {
                int value = Integer.parseInt(userInput);
                if (value >0){
                    this.totalTickets = value;
                    break;
                }else {
                    System.out.println("Total number of tickets must be a positive integer");
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a positive integer");
            }
        }

        // Ticket Release Rate
        while (true){
            System.out.println("Enter Ticket Release Rate");
            String userInput = input.nextLine();
            try {
                int value =  Integer.parseInt(userInput);
                if (value >0){
                    this.ticketReleaseRate = value;
                    break;
                } else {
                    System.out.println("Ticket release rate must be a positive integer");
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a positive integer");
            }
        }

        // Customer Retrieval Rate
        while (true){
            System.out.println("Enter Customer Retrieval Rate");
            String userInput = input.nextLine();

            try {
                int value = Integer.parseInt(userInput);
                if (value >0){
                    this.customerRetrievalRate = value;
                    break;
                } else {
                    System.out.println("Customer retrieval rate must be a positive integer");
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a positive integer");
            }
        }

        // Maximum Ticket Capacity
        while (true){
            System.out.println("Enter Maximum Ticket Capacity");
            String userInput = input.nextLine();
            try {
                int value = Integer.parseInt(userInput);
                if (value >0){
                    this.maxTicketCapacity = value;
                    break;
                } else {
                    System.out.println("Maximum ticket capacity must be a positive integer");
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a positive integer");
            }
        }
    }

    public void saveConfigurationToFile(String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }

    public static Configuration loadConfigurationFromFile(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}
