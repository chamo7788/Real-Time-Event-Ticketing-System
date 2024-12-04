import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
    private static final String CONFIG_FILE = "configuration.json";
    private static List<Thread> vendorThreads = new ArrayList<>();
    private static List<Thread> customerThreads = new ArrayList<>();
    private static List<Vendor> vendors = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static TicketPool ticketPool;

    public static void main(String[] args) {
        Configuration configuration = null;
        Scanner input = new Scanner(System.in);

        // Check if the configuration file exists
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            System.out.println("Previous configuration found.");
            System.out.println("1. Use previous configuration");
            System.out.println("2. Enter new configuration");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine();

            if ("1".equals(choice)) {
                configuration = Configuration.loadConfigurationFromFile(CONFIG_FILE);
                if (configuration == null) {
                    System.out.println("Error loading previous configuration. Please re-enter settings.");
                    configuration = new Configuration();
                    configuration.loadConfiguration();
                    configuration.saveConfigurationToFile(CONFIG_FILE);
                }
            } else if ("2".equals(choice)) {
                configuration = new Configuration();
                configuration.loadConfiguration();
                configuration.saveConfigurationToFile(CONFIG_FILE);
            } else {
                System.out.println("Invalid choice. Defaulting to new configuration.");
                configuration = new Configuration();
                configuration.loadConfiguration();
                configuration.saveConfigurationToFile(CONFIG_FILE);
            }
        } else {
            System.out.println("No previous configuration found. Entering new configuration.");
            configuration = new Configuration();
            configuration.loadConfiguration();
            configuration.saveConfigurationToFile(CONFIG_FILE);
        }

        // Initialize the ticket pool
        ticketPool = new TicketPool(configuration.getMaxTicketCapacity());

        // Initialize Vendors
        int numberOfVendors = configuration.getTicketReleaseRate();
        for (int i = 0; i < numberOfVendors; i++) {
            Vendor vendor = new Vendor(ticketPool, configuration.getTotalTickets());
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            vendors.add(vendor);
            vendorThreads.add(vendorThread);
        }

        // Initialize Customers
        int numberOfCustomers = configuration.getCustomerRetrievalRate();
        for (int i = 0; i < numberOfCustomers; i++) {
            Customer customer = new Customer(ticketPool, configuration.getTotalTickets());
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            customers.add(customer);
            customerThreads.add(customerThread);
        }

        // Start Vendors
        for (Thread t : vendorThreads) {
            t.start();
        }

        // Start Customers
        for (Thread t : customerThreads) {
            t.start();
        }

        // Menu interaction
        while (true) {
            System.out.println("\n*** Ticketing System Menu ***");
            System.out.println("1. View Current Ticket Count");
            System.out.println("2. View Active Threads");
            System.out.println("3. Stop System");
            System.out.println("4. Reload Configuration");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1":
                    System.out.println("Current Ticket Count: " + ticketPool.getCurrentTicketCount());
                    break;
                case "2":
                    System.out.println("Active Vendors: " + vendorThreads.size());
                    System.out.println("Active Customers: " + customerThreads.size());
                    break;
                case "3":
                    stopSystem();
                    break;
                case "4":
                    configuration = Configuration.loadConfigurationFromFile(CONFIG_FILE);
                    if (configuration == null) {
                        System.out.println("Error loading previous configuration. Please re-enter settings.");
                        configuration = new Configuration();
                        configuration.loadConfiguration();
                        configuration.saveConfigurationToFile(CONFIG_FILE);
                    } else {
                        System.out.println("Configuration reloaded successfully.");
                        continue;
                    }
                case "5":
                    stopSystem();
                    System.out.println("Exiting application.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void stopSystem() {
        System.out.println("Stopping all vendors and customers...");

        // Stop all vendors
        for (Vendor vendor : vendors) {
            vendor.stopVendor();
        }

        // Stop all customers
        for (Customer customer : customers) {
            customer.stopCustomer();
        }

        // Interrupt all vendor threads
        for (Thread t : vendorThreads) {
            t.interrupt();
        }

        // Interrupt all customer threads
        for (Thread t : customerThreads) {
            t.interrupt();
        }

        // Wait for all vendor threads to finish
        for (Thread t : vendorThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Wait for all customer threads to finish
        for (Thread t : customerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All vendors and customers stopped.");
    }
}
