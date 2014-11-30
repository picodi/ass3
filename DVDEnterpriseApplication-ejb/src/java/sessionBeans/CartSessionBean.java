/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entites.Cart;
import javax.ejb.Stateless;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless
public class CartSessionBean implements CartSessionBeanLocal {

    @PersistenceContext(name="DVDEnterpriseApplication-ejbPU")
    EntityManager em;

    @Override
    public void addCart(Cart cart) {
        em.persist(cart);
    }

    @Override
    public void editCart(Cart cart) {
        em.merge(cart);
    }

    @Override
    public List<Cart> getActiveCart(int userId) {
        return em.createNamedQuery("Cart.findByUserStatus").setParameter("userId", userId).setParameter("confirmed", 0).getResultList();
    }
    
    @Override
    public Cart getCart(int id){
        return em.find(Cart.class, id);
    }

    @Override
    public void deleteCart(int cartId) {
        em.remove(getCart(cartId));
    }

    @Override
    public Cart getCart(int userId, int bookId) {
        try{
        return (Cart) em.createNamedQuery("Cart.findByUserAndBook").setParameter("userId", userId).setParameter("bookId", bookId).getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
    @Override
    public Cart getActiveCart(int userId, int bookId) {
        try{
        return (Cart) em.createNamedQuery("Cart.findByUserBookStatus").setParameter("userId", userId).setParameter("bookId", bookId).setParameter("confirmed", 0).getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
}
