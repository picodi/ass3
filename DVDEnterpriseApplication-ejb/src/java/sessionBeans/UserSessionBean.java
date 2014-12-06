/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entites.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless(name="UserSessionBean")
public class UserSessionBean implements UserSessionBeanLocal 
{
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "DVDEnterpriseApplication-ejbPU")
    EntityManager em;
    @Override
    public void addUser(User u)
    {
        em.persist(u);
    }

    @Override
    public int checkCredentials(String username, String password) {
        List<User> users = em.createNamedQuery("User.findForLogin").setParameter("username", username).setParameter("password", password).getResultList();
        if (users.size() > 0) {
            return users.get(0).getId();
        } else {
            return -1;
        }
    }
    
    @Override
    public User getUserByUsername(String username) {
        try{
            return (User)em.createNamedQuery("User.findByUsername").setParameter("username", username).getSingleResult();
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return em.createNamedQuery("User.findAll").getResultList();
    }

    @Override
    public User getUser(int id) {
        return em.find(User.class, id);
    }
}
