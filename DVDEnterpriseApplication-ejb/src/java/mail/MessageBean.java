/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author user
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/dest")
})
public class MessageBean implements MessageListener {
    
    public MessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        MapMessage msg;
        String text = "";
        if (message instanceof MapMessage) {
            msg = (MapMessage)message;
            try {
                String title = msg.getString("title");
                String author = msg.getString("author");
                Integer quantity = (Integer)msg.getObject("quantity");
                Integer price = (Integer)msg.getObject("price");
                
                text = "New book added: " + title + " by " + author + "! price: " + price;
            } catch (JMSException ex) {
                Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            return;
        }
        try {
            System.out.println(text);
        } catch (Exception ex) {
            Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
