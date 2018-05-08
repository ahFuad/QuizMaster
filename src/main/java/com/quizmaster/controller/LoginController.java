package com.quizmaster.controller;

import com.quizmaster.database.DBConnection;
import com.quizmaster.encryption.Hashing;
import com.quizmaster.model.Login;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.sql.*;

@ManagedBean(name = "logincontroller")
@RequestScoped
public class LoginController {

    Login login = new Login();

    public LoginController() {
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void performLogIn(){

        String salt = userAvailable();
        if(!salt.isEmpty()){
            Hashing hashing = new Hashing();
            String checkHashedPassword = hashing.performHash(login.getUserPassword(), salt);
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            try {
                PreparedStatement pst = connection.prepareStatement("select user_hashedpassword from qmusers where user_id=?");
                pst.setString(1,login.getUserId());
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    String hashedPassword=rs.getString(1);
                    if(checkHashedPassword.equals(hashedPassword)){
                        System.out.println("LogIn Success :)");
                    } else{
                        System.out.println("Password doesnt match...." +
                                "so Invalid User ID/Password");
                    }
                }
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    private String userAvailable(){
        DBConnection dbConnection = new DBConnection();
        Connection con = dbConnection.getConnection();
        boolean isAvailable=false;
        String salt= "";    //empty string

        try {
            PreparedStatement pst = con.prepareStatement("select user_salt from qmusers where user_id=?");
            pst.setString(1,login.getUserId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                isAvailable=true;
                salt=rs.getString(1);
                System.out.println("Salt fetched from database: "+salt);    //for checking
            } else{
                System.out.println("User not available..." +
                        "so User ID/Password is invalid");
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return salt;
    }

}
