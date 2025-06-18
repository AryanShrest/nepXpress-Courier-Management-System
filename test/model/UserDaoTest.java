package model;

import org.junit.Test;
import static org.junit.Assert.*;

import model.UserDao;
import model.UserData;
import model.LoginRequest;
import org.junit.Assert;

public class UserDaoTest {
    String correctEmail = "testt@gmail.com";
    String correctName = "Testt.user";
    String password = "password";
    UserDao dao = new UserDao();

    @Test
    public void registerWithMeDetails() {
        UserData user = new UserData(correctName, correctEmail, password);
        boolean result = dao.register(user);
        assertTrue("Register should work with unique details", result);
    }

    @Test
    public void registerWithDuplicateDetails() {
        UserData user = new UserData(correctName, correctEmail, password);
        boolean result = dao.register(user);
        assertFalse("Register should fail with duplicate details", result);
    }

    @Test
    public void loginWithCorrectCode() {
        LoginRequest req = new LoginRequest(correctEmail, password);
        UserData user = dao.login(req);
        Assert.assertNotNull("User should not be null", user);
        Assert.assertEquals{"Name shouldnot match",correctName,user.getName());
        Assert.assertEquals("Email should not match",correctEmail,user.getEmail());
        Assert.assertEquals("Password should match",password,user.getPassword());
    @Test
    public void LoginWithInvalidCreds(){
            LoginRequest req=new LoginRequest("abc@test.com","iuoytre");
            UserData user=dao.login(req);
            Assert.assertNull("User shoukd be null", user);
        } 
    }
    }
}
