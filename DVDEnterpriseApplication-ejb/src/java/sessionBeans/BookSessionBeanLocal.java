/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Local;
import java.util.List;
import entites.Book;

/**
 *
 * @author user
 */
@Local
public interface BookSessionBeanLocal {
    
    public void addBook(Book b);
    public Book editBook(Book b);
    public void deleteBook(int id);
    public List getBooks();
    public Book findBook(int id);
}
