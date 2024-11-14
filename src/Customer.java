import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {

    private static final AtomicInteger customerIdGenerator = new AtomicInteger(1);
    private final int customerId;
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private volatile boolean running = true;

    public Customer(TicketPool ticketPool, int customerRetrievalRate){
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerId = customerIdGenerator.getAndIncrement();
    }

    @Override
    public void run(){
        System.out.println("Customer" + customerId+ " started purchasing tickets.");
        while(running){
            try{
                Thread.sleep(1000);

                for (int i = 0; i< customerRetrievalRate; i++){
                    String ticket = ticketPool.removeTicket();
                    if (ticket != null){
                        System.out.println("Customer " + customerId + " purchased " + ticket + ". Remaining tickets: " + ticketPool.getCurrentTicketCount());
                    }else{
                        System.out.println("Customer " + customerId + " failed to purchase a ticket.");
                        break;
                    }
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerId + " interrupted.");
            }
        }
        System.out.println("Customer " + customerId + " stopped.");
    }
    public void stopCustomer(){
        running = false;
    }

}
