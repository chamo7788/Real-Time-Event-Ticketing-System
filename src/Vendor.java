import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable {
    private static final AtomicInteger vendorIdGenerator = new AtomicInteger(1);
    private final int vendorId;
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private volatile boolean running = true;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorIdGenerator.getAndIncrement();
    }

    @Override
    public void run(){
        System.out.println("Vendor " + vendorId + " started releasing tickets.");
        while (running){
            try {
                Thread.sleep(1000);

                List<String> newTickets = new ArrayList<>();
                for (int i = 0; i < ticketReleaseRate; i++){
                    newTickets.add("Ticket-" + System.currentTimeMillis() + "-" + i);
                }

                ticketPool.addTickets(newTickets);
                System.out.println("Vendor " + vendorId + " released " + newTickets.size() + " tickets. Total tickets: " + ticketPool.getCurrentTicketCount());

            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " interrupted.");
            }
        }
        System.out.println("Vendor " + vendorId + " stopped.");
    }

    public void stopVendor(){
        running = false;
    }
}
