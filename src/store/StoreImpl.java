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

public class StoreImpl implements StoreInterface {
    private static final Logger logger = CustomLogger.getLogger(StoreImpl.class);

    private static final String USERS_JSON = "users.json";
    private static final String PRODUCTS_JSON = "products.json";

    public StoreImpl() {
        // Initialize the store with default files if they don't exist
        initializeFile(USERS_JSON);
        initializeFile(PRODUCTS_JSON);
    }

    private void initializeFile(String filePath) {
        try {
            logger.info("Initializing file: " + filePath);
            Files.createFile(Paths.get(filePath));
            Files.write(Paths.get(filePath), "[]".getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            logger.info("File already exists: " + filePath);
            // File already exists or another IO error occurred
        }
    }

    @Override
    public boolean doUserLogin(String username, String password) {
        logger.info("Logging in user: " + username);
        JSONArray users = JsonUtil.readJsonArrayFromFile(USERS_JSON);
        for (Object o : users) {
            JSONObject user = (JSONObject) o;
            String storedUsername = (String) user.get("username");
            String storedPassword = (String) user.get("password");
            if (storedUsername.equals(username) && storedPassword.equals(password)) {
                logger.info("User logged in: " + username);
                return true;
            }
        }
        logger.info("User login failed: " + username);
        return false;
    }

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

    @Override
    public JSONArray showProductsInShop() {
        logger.info("Showing all products");
        return JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);
    }

    @Override
    public boolean deleteProductInShop(String productName) {
        logger.info("Deleting product: " + productName);
        JSONArray products = JsonUtil.readJsonArrayFromFile(PRODUCTS_JSON);
        products.removeIf(product -> ((JSONObject) product).get("name").equals(productName));
        JsonUtil.writeJsonArrayToFile(products, PRODUCTS_JSON);
        logger.info("Product deleted: " + productName);
        return true;
    }


}
