import java.util.List;
import java.util.LinkedList;

public class TicketPool {
    private final List<String> tickets;
    private final int maxCapacity;

    public TicketPool(int maxCapacity){
        this.tickets = new LinkedList<>();
        this.maxCapacity = maxCapacity;
    }

    // Synchronized method to add tickets
    public synchronized void addTickets(List<String> newTickets){
        while (tickets.size() + newTickets.size() > maxCapacity){
            try {
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Vendor interrupted while waiting to add tickets.");
                return;
            }
        }
        tickets.addAll(newTickets);
        notifyAll();
    }

    // Synchronized method to remove a ticket
    public synchronized String removeTicket(){
        while (tickets.isEmpty()){
            try {
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Customer interrupted while waiting to retrieve tickets.");
                return null;
            }
        }
        String ticket = tickets.remove(0);
        notifyAll();
        return ticket;
    }

    // Method to get current number of tickets
    public synchronized int getCurrentTicketCount(){
        return tickets.size();
    }
    public synchronized List<String> getAllTickets(){
        return new LinkedList<>(tickets);
    }

}
