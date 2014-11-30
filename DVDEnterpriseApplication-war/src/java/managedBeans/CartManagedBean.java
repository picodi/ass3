/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entites.Book;
import entites.Cart;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import sessionBeans.BookSessionBeanLocal;
import sessionBeans.CartSessionBeanLocal;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class CartManagedBean {
    
    private List<Cart> carts;
    
    private String message;
    private int quantity = 1;
    private int editable;
    
    final int NOT_CONFIRMED = 0;
    final int CONFIRMED = 1;
    
    @EJB
    CartSessionBeanLocal  cartBean;
    
    @EJB
    BookSessionBeanLocal bookBean;
    
    public CartManagedBean() {
    }
    
    public void loadCart(int id){
        carts = cartBean.getActiveCart(id);
        if (carts == null) {
            carts = new ArrayList<Cart>();
        }
    }
    public void clearMessage()
    {
        message = "";
    }
    
    
    public void delete(int cartId, int userId){
        cartBean.deleteCart(cartId);
        loadCart(userId);
        message = "Successful delete!";
    }
    
    public void edit(int cartId, int userId){
        Cart cart = cartBean.getCart(cartId);
        cart.setQuantity(quantity);
        message = "Edit successful!";
        editable = -1;
        cartBean.editCart(cart);
        loadCart(userId);
    }
    
    public String add(int bookId, int userId){
        Cart cart = cartBean.getActiveCart(userId, bookId);
        if(cart == null){
            cart = new Cart();
            cart.setBookId(bookId);
            cart.setQuantity(quantity);
            quantity = 1;
            cart.setUserId(userId);
            cart.setConfirmed(NOT_CONFIRMED);
            cartBean.addCart(cart);
        } else {
            cart.setQuantity(cart.getQuantity() + quantity);
            cartBean.editCart(cart);
        }
        return null;
    }
    
    public String buy(int userId){
        loadCart(userId);
        for (Cart cart : carts){
            if (cart.getQuantity() > bookBean.findBook(cart.getBookId()).getQuantity() ){
                String title = bookBean.findBook(cart.getBookId()).getTitle();
                message = "Insufficient stock for: " + title;
                return null;
            }
        }
        for (Cart cart : carts){
            Book book = bookBean.findBook(cart.getBookId());
            message = Integer.toString(book.getId());
            book.setQuantity(book.getQuantity() - cart.getQuantity());
            bookBean.editBook(book);
            cart.setConfirmed(CONFIRMED);
            cartBean.editCart(cart);
        }
        message = "Order placed";
        return "userView";
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public boolean isEditable(int id){
        return editable==id;
    }
    
}
