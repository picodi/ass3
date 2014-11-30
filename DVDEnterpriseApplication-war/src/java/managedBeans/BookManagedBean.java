/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entites.Book;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import sessionBeans.BookSessionBeanLocal;

/**
 *
 * @author user
 */
@ManagedBean (name="bookManagedBean")
@SessionScoped
public class BookManagedBean {

    private List<Book> books;
    
    private String message;
    private int editable;
    private String title;
    private String author;
    private Integer quantity;
    private Integer price;
    
    @EJB
    BookSessionBeanLocal bookBean;
    
    public BookManagedBean() {
        message = "";
        editable = -1;
    }
    
    public void loadBooks(){
        books = bookBean.getBooks();
        if(books == null){
            books = new ArrayList<Book>();
        }
    }
    
    public void edit(int bookId){
        if(title.trim().isEmpty()){
            message = "Complete the fields!";
        } else {
            Book book = bookBean.findBook(bookId);
            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);
            message = "Edit successful!";
            editable = -1;
            bookBean.editBook(book);
            loadBooks();
        }
    }
    
    public boolean isEditable(int id){
        return editable==id;
    }

    public String add(){
        if(title.trim().isEmpty()){
            message = "Complete the fields!";
        } else {
            Book book = new Book();
            book.setAuthor(author);
            book.setTitle(title);
            book.setQuantity(quantity);
            book.setPrice(price);
            bookBean.addBook(book);
            message = "One book added to the collection";
            loadBooks();
        }
        return null;
    }

    public void delete(int bookId){
        bookBean.deleteBook(bookId);
        loadBooks();
        message = "Successful delete!";
    }

    public void setEditable(int editable) {
        this.editable = editable;
        Book book = bookBean.findBook(editable);
        title = book.getTitle();
        author = book.getAuthor();
        quantity = book.getQuantity();
    }
    
    public Book getBook(int id){
        return bookBean.findBook(id);
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    
}
