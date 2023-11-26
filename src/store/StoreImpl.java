package store;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.JsonUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class StoreImpl implements StoreInterface {
    private final String usersFilePath = "users.json";
    private final String productsFilePath = "products.json";

    public StoreImpl() {
        // Initialize the store with default files if they don't exist
        initializeFile(usersFilePath);
        initializeFile(productsFilePath);
    }

    private void initializeFile(String filePath) {
        try {
            Files.createFile(Paths.get(filePath));
            Files.write(Paths.get(filePath), "[]".getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            // File already exists or another IO error occurred
        }
    }

    @Override
    public boolean doUserLogin(String username, String password) {
        JSONArray users = JsonUtil.readJsonArrayFromFile(usersFilePath);
        for (Object o : users) {
            JSONObject user = (JSONObject) o;
            String storedUsername = (String) user.get("username");
            String storedPassword = (String) user.get("password");
            if (storedUsername.equals(username) && storedPassword.equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean registerNewUser(String username, String password, String email, String phone) {
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("email", email);
        newUser.put("phone", phone);

        JSONArray users = JsonUtil.readJsonArrayFromFile(usersFilePath);
        users.add(newUser);
        JsonUtil.writeJsonArrayToFile(users, usersFilePath);
        return true;
    }

    @Override
    public boolean insertProductInShop(String name, double price) {
        JSONObject newProduct = new JSONObject();
        newProduct.put("name", name);
        newProduct.put("price", price);

        JSONArray products = JsonUtil.readJsonArrayFromFile(productsFilePath);
        products.add(newProduct);
        JsonUtil.writeJsonArrayToFile(products, productsFilePath);
        return true;
    }

    @Override
    public JSONArray showProductsInShop() {
        return JsonUtil.readJsonArrayFromFile(productsFilePath);
    }

    @Override
    public boolean deleteProductInShop(String productName) {
        JSONArray products = JsonUtil.readJsonArrayFromFile(productsFilePath);
        products.removeIf(product -> ((JSONObject) product).get("name").equals(productName));
        JsonUtil.writeJsonArrayToFile(products, productsFilePath);
        return true;
    }


}
