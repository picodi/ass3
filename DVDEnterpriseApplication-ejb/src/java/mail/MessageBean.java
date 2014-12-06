/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import com.sun.mail.smtp.SMTPTransport;
import entites.Book;
import entites.User;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import sessionBeans.BookSessionBeanLocal;
import sessionBeans.UserSessionBeanLocal;

/**
 *
 * @author user
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/dest")
})
public class MessageBean implements MessageListener {
        
    @EJB(beanName="BookSessionBean")
    BookSessionBeanLocal bookSessionBean;
    @EJB(beanName="UserSessionBean")
    UserSessionBeanLocal userSessionBean;
    
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
                String subject = "New Book";
                body = "New book added: " + title + " by " + author + "! price: " + price;
                
                System.out.println(body);
                javax.naming.Context c = new javax.naming.InitialContext();
                
                book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setQuantity(quantity);
                book.setPrice(price);
                
                bookSessionBean.addBook(book);
                ArrayList<String> emails = getEmails();
                for (String email : emails) {
                    System.out.println(email);
                    send("dpicos@pitechnologies.ro", "itchiban92@", email, "", subject, body);
                }
                
            } catch( Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void send(String username, String password, String recipientEmail, String ccEmail, String title, String message) {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        /*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
        */
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        try {
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            if (ccEmail.length() > 0) {
                msg.setRecipients(RecipientType.CC, InternetAddress.parse(ccEmail, false));
            }
            msg.setSubject(title);
            msg.setText(message, "utf-8");
            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

            t.connect("smtp.gmail.com", username, password);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (MessagingException ex) {
            Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }


      
    }
    
    public ArrayList<String> getEmails()
    {
        ArrayList<String> emails = new ArrayList<>();
        userSessionBean.getAllUsers();
        for (User user : userSessionBean.getAllUsers()) {
            if (user.getEmail().compareTo("") != 0) {
                emails.add(user.getEmail());
            }
        }
        return emails;
    }
    
}
