package store;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.CustomLogger;
import utils.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

/**
 * Implementation of StoreInterface providing functionalities for a store.
 * This includes user authentication, product management, and data persistence using JSON files.
 */
public class StoreImpl implements StoreInterface {
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
            logger.info("Initializing file: " + filePath);
            Files.createFile(Paths.get(filePath));
            Files.write(Paths.get(filePath), "[]".getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            logger.info("File already exists: " + filePath);
        }
    }

    /**
     * Authenticates a user by checking the provided username and password against the stored data.
     *
     * @param username the username to authenticate.
     * @param password the password to authenticate.
     * @return true if authentication is successful, false otherwise.
     */
    @Override
    public boolean doUserLogin(String username, String password) {
        logger.info("Logging in user: " + username);
        JSONArray users = JsonUtil.readJsonArrayFromFile(USERS_JSON);
        for (Object o : users) {
            JSONObject user = (JSONObject) o;
            if (username.equals(user.get("username")) && password.equals(user.get("password"))) {
                logger.info("User logged in: " + username);
                return true;
            }
        }
        logger.info("User login failed: " + username);
        return false;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param username the username of the new user.
     * @param password the password of the new user.
     * @param email    the email of the new user.
     * @param phone    the phone number of the new user.
     * @return true if registration is successful.
     */
    @Override
    public boolean registerNewUser(String username, String password, String email, String phone) {
        logger.info("Registering new user: " + username);
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("email", email);
        newUser.put("phone", phone);

        JSONArray users = JsonUtil.readJsonArrayFromFile(USERS_JSON);
        users.add(newUser);
        JsonUtil.writeJsonArrayToFile(users, USERS_JSON);
        logger.info("User registered: " + username);
        return true;
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
        logger.info("Inserting product: " + name + " with price: " + price);
        JSONObject newProduct = new JSONObject();
        newProduct.put("name", name);
        newProduct.put("price", price);

        JSONArray products = JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);
        products.add(newProduct);
        JsonUtil.writeJsonArrayToFile(products, PRODUCTS_JSON);
        logger.info("Product inserted: " + name + " with price: " + price);
        return true;
    }

    /**
     * Retrieves and shows all products in the shop.
     *
     * @return JSONArray containing all the products.
     */
    @Override
    public JSONArray showProductsInShop() {
        logger.info("Showing all products");
        return JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);
    }

    /**
     * Deletes a product from the shop.
     *
     * @param productName the name of the product to delete.
     * @return true if the product is successfully deleted.
     */
    @Override
    public boolean deleteProductInShop(String productName) {
        logger.info("Deleting product: " + productName);
        JSONArray products = JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);
        products.removeIf(product -> productName.equals(((JSONObject) product).get("name")));
        JsonUtil.writeJsonArrayToFile(products, PRODUCTS_JSON);
        logger.info("Product deleted: " + productName);
        return true;
    }
}