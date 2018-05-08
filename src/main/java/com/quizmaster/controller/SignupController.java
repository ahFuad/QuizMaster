package com.quizmaster.controller;

import com.quizmaster.database.DBConnection;
import com.quizmaster.model.Signup;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ManagedBean(name = "signupcontroller")
@RequestScoped
public class SignupController {
    Signup signup = new Signup();

    public SignupController() {
    }

    public Signup getSignup() {
        return signup;
    }

    public void setSignup(Signup signup) {
        this.signup = signup;
    }

    public void performSignup(){

    }

}
