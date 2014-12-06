/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import entites.Book;
import entites.User;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import sessionBeans.BookSessionBean;
import sessionBeans.BookSessionBeanLocal;
import sessionBeans.UserSessionBean;

/**
 *
 * @author user
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/dest")
})
public class MessageBean implements MessageListener {
    
    String smtpServer = "smtp.gmail.com";
    String subject = "New Book";
    String subjectF = "Fayul";
    String bodyF = "Something went wrong";
    String from = "test@test.test";
    String to = "dpicos@pitechnologies.ro";
    
    @EJB(beanName="BookSessionBean")
    BookSessionBeanLocal bookSessionBean;
    
    public MessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("on the other side2");
        TextMessage text = (TextMessage)message;
        String msgText = "";
        try {
            msgText = text.getText();
            System.out.println(msgText);
        } catch (JMSException ex) {
            Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        String body = "";
        if (msgText.contains("/")) {
            String[] split = msgText.split("/");
            String title = split[0];
            String author = split[1];
            Integer quantity = Integer.parseInt(split[2]);
            Integer price = Integer.parseInt(split[3]);
            
            Book book;
            try {
                
                body = "New book added: " + title + " by " + author + "! price: " + price;
                
                System.out.println(body);
                javax.naming.Context c = new javax.naming.InitialContext();
                
                book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setQuantity(quantity);
                book.setPrice(price);
                
                bookSessionBean.addBook(book);
            //    sendEmail(smtpServer, to, from, subject, body);
            } catch(javax.naming.NamingException e) {
                e.printStackTrace();
            } catch( Exception e) {
            //    sendEmail(smtpServer, to, from, subjectF, bodyF);
            }
            
        } else {
            return;
        }
    }
    /*
    public void sendEmail(String smtpServer, String to2, String from2, String subject, String body)
    {
      String to = "abcd@gmail.com";
      String from = "dpicos@pitechnologies.ro";
      String host = "localhost";
      Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", host);
      Session session = Session.getDefaultInstance(properties);

      try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
         mex.printStackTrace();
      }
    }
    */
    
    public ArrayList<String> getEmails()
    {
        ArrayList<String> emails = new ArrayList<>();
    /*    UserSessionBean userSessionBean;
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            userSessionBean = (UserSessionBean)c.lookup(BookSessionBean.class.getName());
            for (User user : userSessionBean.getAllUsers()) {
                if (user.getEmail().compareTo("") != 0) {
                    emails.add(user.getEmail());
                }
            }
        } catch(javax.naming.NamingException e) {
                e.printStackTrace();
        }
       */ 
        return emails;
    }
    
}
