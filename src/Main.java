import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Thread> vendorThreads = new ArrayList<>();
    private static List<Thread> customerThreads = new ArrayList<>();
    private static List<Vendor> vendors = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static TicketPool ticketPool;

    public static void main(String[] args){
        Configuration configuration = new Configuration();
        configuration.loadConfiguration();

        ticketPool = new TicketPool(configuration.getMaxTicketCapacity());

        // Initialize Vendors
        int numberOfVendors = configuration.getTicketReleaseRate();

        for (int i = 0; i< numberOfVendors; i++){
            Vendor vendor = new Vendor(ticketPool, configuration.getTotalTickets());
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            vendors.add(vendor);
            vendorThreads.add(vendorThread);
        }

        // Initialize Customers
        int numberOfCustomers = configuration.getCustomerRetrievalRate();
        for (int i = 0; i<numberOfCustomers; i++){
            Customer customer = new Customer(ticketPool, configuration.getTotalTickets());
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            customers.add(customer);
            customerThreads.add(customerThread);
        }

        // Start Vendors
        for (Thread t: vendorThreads){
            t.start();
        }

        // Start Customers
        for (Thread t: customerThreads){
            t.start();
        }

        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("\n*** Ticketing System Menu ***");
            System.out.println("1. View Current Ticket Count");
            System.out.println("2. Stop System");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
            String choice = input.nextLine();

            switch (choice){
                case "1":
                    System.out.println("Current Ticket Count: " + ticketPool.getCurrentTicketCount());
                    break;
                case "2":
                    stopSystem();
                    break;
                case "3":
                    stopSystem();
                    System.exit(0);
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void stopSystem(){
        System.out.println("Stopping all vendors and customers...");

        for (Vendor vendor: vendors){
            vendor.stopVendor();
        }
        for (Customer customer: customers){
            customer.stopCustomer();
        }

        for (Thread t : vendorThreads) {
            t.interrupt();
        }
        for (Thread t : customerThreads) {
            t.interrupt();
        }

        for (Thread t: vendorThreads){
            try {
                t.join();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        for (Thread t: customerThreads){
            try {
                t.join();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All vendors and customers stopped.");
    }
}
