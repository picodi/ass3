/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entites.User;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface UserSessionBeanLocal 
{
    public void addUser(User u);
    public int checkCredentials(String username, String password);
    public User getUserByUsername(String username);
    public User getUser(int id);
    public List<User> getAllUsers();
}
