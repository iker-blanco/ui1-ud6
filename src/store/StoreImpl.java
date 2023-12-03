
package store;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.CustomLogger;
import utils.JsonUtil;

import java.io.IOException;
import java.io.Serializable; // Importa la interfaz Serializable
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Implementation of StoreInterface providing functionalities for a store.
 * This includes user authentication, product management, and data persistence using JSON files.
 */
public class StoreImpl implements StoreInterface, Serializable { // Implementa Serializable
    private static final long serialVersionUID = 1L; // Añade un número de versión

    private static final Logger logger = CustomLogger.getLogger(StoreImpl.class);

    private static final String USERS_JSON = "users.json";
    private static final String PRODUCTS_JSON = "products.json";
    
    

    /**
     * Constructor for StoreImpl.
     * Initializes the store by ensuring necessary JSON files exist.
     */
    public StoreImpl() {
        initializeFile(USERS_JSON);
        initializeFile(PRODUCTS_JSON);
    }

    /**
     * Initializes a file if it does not already exist.
     *
     * @param filePath the path to the file to be initialized.
     */
    private void initializeFile(String filePath) {
        try {
            logger.info("Iniciando archivo: " + filePath);
            Files.createFile(Paths.get(filePath));
            Files.write(Paths.get(filePath), "[]".getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            logger.info("El archivo ya existe: " + filePath);
        }
    }

    /**
     * Authenticates a user by checking the provided username and password against the stored data.
     *
     * @param username the username to authenticate.
     * @param password the password to authenticate.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean doUserLogin(String username, String password) {
        JSONArray users = JsonUtil.readJsonArrayFromFile(USERS_JSON);

        for (Object userObj : users) {
            JSONObject user = (JSONObject) userObj;
            String storedUsername = (String) user.get("Usuario");
            String storedPassword = (String) user.get("Contraseña");

            if (username.equals(storedUsername) && password.equals(storedPassword)) {
                logger.info("Usuario logueado: " + username);
                return true;
            }
        }

        logger.info("Error iniciando sesion del usuario: " + username);
        return false;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param username      the username of the new user.
     * @param password      the password of the new user.
     * @param email         the email of the new user.
     * @param confirmEmail  the confirmation of the email.
     * @param phone         the phone number of the new user.
     * @return true if registration is successful.
     */
    public boolean registerNewUser(String username, String password, String email, String checkEmail,String phone) {
        // Validate user information
        if (!isValidUsername(username) || !isValidPassword(password) || !isValidEmail(email, checkEmail) || !isValidPhone(phone)) {
            System.out.println("Error: Información de usuario no válida.");
            return false;
        }

        // Rest of the logic to register the new user
        System.out.println("Registrando nuevo usuario: " + username);
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("email", email);
        newUser.put("phone", phone);

        JSONArray users = JsonUtil.readJsonArrayFromFile(USERS_JSON);
        users.add(newUser);
        JsonUtil.writeJsonArrayToFile(users, USERS_JSON);
        System.out.println("Usuario registrado: " + username);

        return true;
    }

    // Validation helper methods

    private boolean isValidUsername(String username) {
        // Implement username validation logic
        // For example, check if it is alphanumeric
        return username.matches("^[a-zA-Z0-9]+$");
    }

    private boolean isValidPassword(String password) {
        // Implement password validation logic
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@*&%]).{8,}$";
        return password.matches(passwordRegex);

    }

    private boolean isValidEmail(String email, String confirmEmail) {
        // Implement email validation logic using regex
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (!email.matches(emailRegex)) {
            System.out.println("Error: Formato de correo electrónico no válido.");
            return false;
        }

        // Comparar emails de manera explícita
        if (!email.equals(confirmEmail)) {
            System.out.println("Error: Los correos electrónicos no coinciden.");
            return false;
        }

        return true;
    }

    private boolean isValidPhone(String phone) {
        // Implement phone validation logic
       
        return phone.matches("^\\d{9,}$");
    }


    
    /**
     * Inserts a new product into the shop.
     *
     * @param name  the name of the product.
     * @param price the price of the product.
     * @return true if the product is successfully inserted.
     */
    @Override
    public boolean insertProductInShop(String name, double price) {
        // Generar un ID único para el nuevo producto
        String id = UUID.randomUUID().toString();

        logger.info("Producto añadido: " + name + " con precio: " + price + " y su ID: " + id);

        // Crear un nuevo objeto Product
        Product newProduct = new Product(id, name, price);

        // Convertir el objeto Product a JSONObject
        JSONObject productJson = new JSONObject();
        productJson.put("id", newProduct.getId());
        productJson.put("name", newProduct.getName());
        productJson.put("price", newProduct.getPrice());

        // Leer el archivo JSON existente
        JSONArray products = JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);

        // Agregar el nuevo producto al array JSON
        products.add(productJson);

        // Escribir el array actualizado en el archivo
        boolean writeResult = JsonUtil.writeJsonArrayToFile(products, PRODUCTS_JSON);

        if (writeResult) {
            logger.info("Producto añadido: " + name + " with price: " + price + " and ID: " + id);
            return true;
        } else {
            logger.severe("Error al añadir producto: " + name);
            return false;
        }
    }

    /**
     * Retrieves and shows all products in the shop.
     *
     * @return JSONArray containing all the products.
     */
    @Override
public List<Product> showProductsInShop() {
    logger.info("Mostrando todos los productos");
    
    // Leer el array JSON de productos
    JSONArray productsJson = JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);

    // Crear una lista para almacenar los objetos Product
    List<Product> products = new ArrayList<>();

    // Convertir cada objeto JSON a un objeto Product
    for (Object o : productsJson) {
        JSONObject productJson = (JSONObject) o;
        String id = (String) productJson.get("id");
        String name = (String) productJson.get("name");
        double price = (double) productJson.get("price");

        Product product = new Product(id, name, price);
        products.add(product);
    }

    return products;
}

    /**
     * Deletes a product from the shop.
     *
     * @param productName the name of the product to delete.
     * @return true if the product is successfully deleted.
     */
    @Override
    public boolean deleteProductInShop(String productName) {
        logger.info("Eliminando producto: " + productName);
        JSONArray products = JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);
        products.removeIf(product -> productName.equals(((JSONObject) product).get("name")));
        JsonUtil.writeJsonArrayToFile(products, PRODUCTS_JSON);
        logger.info("Producto eliminado: " + productName);
        return true;
    }

	
}