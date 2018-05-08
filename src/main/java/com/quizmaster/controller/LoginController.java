package com.quizmaster.controller;

import com.quizmaster.database.DBConnection;
import com.quizmaster.model.Login;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        Connection connection = null;

        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from qmusers");
            System.out.println("...............");
            while (resultSet.next()){
                System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" ");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
