import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.FileWriter;
import java.io.IOException;

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
        JSONArray users = readJsonArrayFromFile(usersFilePath);
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

        JSONArray users = readJsonArrayFromFile(usersFilePath);
        users.add(newUser);
        writeJsonArrayToFile(users, usersFilePath);
        return true;
    }

    @Override
    public boolean insertProductInShop(String name, double price) {
        JSONObject newProduct = new JSONObject();
        newProduct.put("name", name);
        newProduct.put("price", price);

        JSONArray products = readJsonArrayFromFile(productsFilePath);
        products.add(newProduct);
        writeJsonArrayToFile(products, productsFilePath);
        return true;
    }

    @Override
    public JSONArray showProductsInShop() {
        return readJsonArrayFromFile(productsFilePath);
    }

    @Override
    public boolean deleteProductInShop(String productName) {
        JSONArray products = readJsonArrayFromFile(productsFilePath);
        products.removeIf(product -> ((JSONObject) product).get("name").equals(productName));
        writeJsonArrayToFile(products, productsFilePath);
        return true;
    }

    private JSONArray readJsonArrayFromFile(String filePath) {
        JSONParser parser = new JSONParser();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Object obj = parser.parse(content);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private boolean writeJsonArrayToFile(JSONArray array, String filePath) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
