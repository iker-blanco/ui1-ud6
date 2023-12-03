package store;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StoreInterface extends Remote {
    
    // Registers a new user with the provided details
   // boolean registerNewUser(String username, String password, String email, String phone) throws RemoteException;

    // Inserts a new product into the shop
    boolean insertProductInShop(String productName, double price) throws RemoteException;

    // Shows all products in the shop
    List<Product> showProductsInShop() throws RemoteException;

    // Deletes a product from the shop by its ID
    boolean deleteProductInShop(String productId) throws RemoteException;
    
 // En StoreInterface
    boolean registerNewUser(String username, String password, String email, String checkEmail, String phone) throws RemoteException;

 // En StoreInterface
    boolean doUserLogin(String username, String password) throws RemoteException;

}
