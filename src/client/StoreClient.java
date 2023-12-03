package client;

import store.Product;
import store.StoreInterface;
import utils.CustomLogger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class StoreClient {
    private static final Logger logger = CustomLogger.getLogger(StoreClient.class);

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            StoreInterface store = (StoreInterface) registry.lookup("StoreService");

            Scanner scanner = new Scanner(System.in);

            // Menú pri2ncipal
            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Registrarse (newUser)");
                System.out.println("2. Iniciar Sesión (doUserLogin)");
                System.out.println("3. Salir");
                System.out.print("Opción: ");

                int option = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (option) {
                    case 1:
                        // Registrarse
                        newUser(store, scanner);
                        break;
                    case 2:
                        // Iniciar Sesión
                        if(doUserLogin(store, scanner)) {
                        	logger.info("Inicio de sesión exitoso.");
                        	loggedInMenu(store, scanner);
                        }
                        else {
                        	logger.info("Inicio de sesión fallido.");
                        }
                        break;
                    case 3:
                        // Salir del programa
                        System.out.println("¡Hasta luego!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            }

        } catch (Exception e) {
            logger.severe("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void newUser(StoreInterface store, Scanner scanner) throws RemoteException {
        System.out.println("Bienvenido a la creación de cuenta. Por favor, complete la información:");

        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Confirmar email: ");
        String confirmEmail = scanner.nextLine();

        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();

        // Llamar al método remoto para registrar un nuevo usuario
        boolean registrationSuccess = store.registerNewUser(username, password, email, confirmEmail, phone);

        // Manejar el resultado del registro
        if (registrationSuccess) {
            logger.info("Registro exitoso para el usuario: " + username);
        } else {
            logger.severe("Registro fallido para el usuario: " + username);
        }
    }

    private static boolean doUserLogin(StoreInterface store, Scanner scanner) throws RemoteException {
        System.out.println("Bienvenido al inicio de sesión. Por favor, ingrese la información:");

        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        // Llamar al método remoto para realizar el inicio de sesión
        boolean loginSuccess = store.doUserLogin(username, password);

        
        return loginSuccess;
    }
    
    private static void loggedInMenu(StoreInterface store, Scanner scanner) {
        System.out.println("Inicio de sesión exitoso. ¡Bienvenido!");

        while (true) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Insertar Producto (insertProductInShop)");
            System.out.println("2. Mostrar Productos (showProductsInShop)");
            System.out.println("3. Borrar Producto (deleteProductInShop)");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Opción: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (option) {
                case 1:
                    // Insertar Producto
                    System.out.print("Introduzca el nombre del producto: ");
                    String productName = scanner.nextLine();
                    System.out.print("Introduzca el precio: ");
                    double price = scanner.nextDouble(); // Asegúrate de manejar la entrada correctamente
                    scanner.nextLine(); // Consumir la nueva línea
        
                    try {
                        boolean productAdded = store.insertProductInShop(productName, price);
                        if (productAdded) {
                            System.out.println("Producto añadido correctamente");
                        } else {
                            System.out.println("Error al añadir producto.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error al añadir el producto: " + e.getMessage());
                    }
                    break;
        
                case 2:
                    // Mostrar Productos
                    try {
                        List<Product> products = store.showProductsInShop();
                        for (Product product : products) {
                            System.out.println(product);
                        }
                    } catch (Exception e) {
                        System.out.println("Error reciviendo productos: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Introduzca el ID del producto a eliminar: ");
                    String productId = scanner.nextLine();
        
                    try {
                        boolean productDeleted = store.deleteProductInShop(productId);
                        if (productDeleted) {
                            System.out.println("Poducto eliminado correctamente");
                        } else {
                            System.out.println("Error al eliminar el producto.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error eliminando producto: " + e.getMessage());
                    }
                    break;
                case 4:
                    // Cerrar Sesión
                    System.out.println("Cerrando sesión. ¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}
