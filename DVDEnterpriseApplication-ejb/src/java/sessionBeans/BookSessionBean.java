/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entites.Book;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless(name="BookSessionBean")
public class BookSessionBean implements BookSessionBeanLocal {

    @PersistenceContext(unitName = "DVDEnterpriseApplication-ejbPU")
    EntityManager em;
    @Override
    public void addBook(Book b) {
        em.persist(b);
    }

    @Override
    public List getBooks() {
        return em.createNamedQuery("Book.findAll").getResultList();
    }

    @Override
    public Book findBook(int id) {
        return em.find(Book.class, id);
    }

    @Override
    public Book editBook(Book b) {
        Book result = em.merge(b); 
        return result; 
    }

    @Override
    public void deleteBook(int id) {
        em.remove(findBook(id));
    }
}
