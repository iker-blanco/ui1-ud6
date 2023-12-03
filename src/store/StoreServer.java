package store;

import utils.CustomLogger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class StoreServer {
    private static final Logger logger = CustomLogger.getLogger(StoreServer.class);
    public static void main(String[] args) {


        try {
            StoreInterface stub = new StoreImpl();

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("StoreService", stub);
            logger.info("Servidor iniciado.");

            // Keep the server running
            while (true) {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    logger.severe("Servidor interrumpido: " + e.toString());
                }
            }
        } catch (Exception e) {
            logger.severe("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
