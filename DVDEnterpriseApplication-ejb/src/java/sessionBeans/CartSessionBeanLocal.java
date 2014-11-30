/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entites.Cart;
import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author user
 */
@Local
public interface CartSessionBeanLocal {
    
    void addCart(Cart cart);
    
    void editCart(Cart cart);
    
    void deleteCart(int cartId);
    
    List<Cart> getActiveCart(int userId);
    
    Cart getCart(int id);
    
    Cart getCart(int user_id, int book_id);
    
    Cart getActiveCart(int user_id, int book_id);
}
