package store;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StoreServer {
    public static void main(String[] args) {
        try {
            StoreInterface stub = new StoreImpl();

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("StoreService", stub);
            System.out.println("Server ready");

            // Keep the server running
            while (true) {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    System.err.println("Server interrupted: " + e.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}