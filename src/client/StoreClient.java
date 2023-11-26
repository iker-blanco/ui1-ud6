package client;

import store.StoreImpl;
import utils.CustomLogger;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Client class for interacting with the StoreImpl.
 * This client allows for user registration via a command line interface.
 */
public class StoreClient {
    private static final Logger logger = CustomLogger.getLogger(StoreClient.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StoreImpl store = new StoreImpl();

        logger.info("Starting Store Registration System");
        logger.info("Enter username: ");
        String username = scanner.nextLine();
        logger.info("Enter password: ");
        String password = scanner.nextLine();
        logger.info("Enter email: ");
        String email = scanner.nextLine();
        logger.info("Enter phone: ");
        String phone = scanner.nextLine();

        boolean registrationSuccess = store.registerNewUser(username, password, email, phone);

        if (registrationSuccess) {
            logger.info("Registration successful for user: " + username);
        } else {
            logger.severe("Registration failed for user: " + username);
        }
    }
}