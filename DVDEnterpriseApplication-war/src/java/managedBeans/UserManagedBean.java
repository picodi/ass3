
package managedBeans;

import entites.User;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import sessionBeans.UserSessionBeanLocal;

@ManagedBean (name="userManagedBean")
@SessionScoped
public class UserManagedBean implements Serializable{
    /**
    * Creates a new instance of UserBean
    */
    private User user; 
    
    private String username;
    private String password;
    private String message;
    private String email;
    final int USER_ROLE = 0;
    final int ADMIN_ROLE = 1;
    
    @EJB UserSessionBeanLocal userSessionBean;
    
    public String getCompleted(){
        message = password;
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setRole(USER_ROLE);
        userSessionBean.addUser(u);
        return "user";
    }
    
    public String register()
    {
        if(username.isEmpty() || password.isEmpty()){
            message = "Please complete both fields";
            return null;
        } else if(userSessionBean.getUserByUsername(username) != null) {
            message = "Username taken. Try another one";
            return null;
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole(USER_ROLE);
            userSessionBean.addUser(newUser);
            user = userSessionBean.getUserByUsername(username);
            return "userView";
        }
    }
    
    public String login(){        
        int userId = userSessionBean.checkCredentials(username, password);
        if (userId > 0) {
            User loggedUser = userSessionBean.getUser(userId);
            user = loggedUser;
            if(loggedUser.getRole() == ADMIN_ROLE){
                return "adminView";
            } else {
                return "userView";
            }
        } else {
            message = "Login failed!";
            return null;
        }
    }
    
    public void messages(String message)
    {
    
    }
    
    public String logOut(){
        user = null;
        message = null;
        return "login";
    }
    
    public void secure() throws IOException {
        if(user == null || user.getRole() != ADMIN_ROLE) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }
    
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setUser(User user)
    {
        this.user = user;
    }
    
    public User getUser()
    {
        return this.user;
    }
}