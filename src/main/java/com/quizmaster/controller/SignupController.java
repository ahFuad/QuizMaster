package com.quizmaster.controller;

import com.quizmaster.database.DBConnection;
import com.quizmaster.encryption.Hashing;
import com.quizmaster.model.Signup;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.*;

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
        if(signup.getUserPassword().equals(signup.getUserPasswordRepeat()) && !checkExistence()){
            Hashing hashing = new Hashing();
            //password encryption
            String salt=hashing.generateRandomSalt();
            String hashedPassword=hashing.performHash(signup.getUserPassword(),salt);
            System.out.println("hashedPassword: "+hashedPassword);  //for checking
            DBConnection dbconnection = new DBConnection();
            Connection con = dbconnection.getConnection();
            try{
                //using prepared statement for value insertion in database
                PreparedStatement pst = con.prepareStatement("insert into qmusers" +
                        "(user_id,user_email,user_hashedpassword,user_salt) " +
                        "values(?,?,?,?)");
                pst.setString(1,signup.getUserId());
                pst.setString(2,signup.getUserEmail());
                pst.setString(3,hashedPassword);
                pst.setString(4,salt);
                pst.executeUpdate();
                System.out.println("Data inserted..");  //for checking
                pst.close();
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                try {
                    //always gets executed
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private boolean checkExistence(){
        DBConnection dbConnection = new DBConnection();
        Connection con = dbConnection.getConnection();
        PreparedStatement pst=null;
        boolean isExist=false;
        try {
            pst = con.prepareStatement("select user_id from qmusers where user_id=? or user_email=?");
            pst.setString(1,signup.getUserId());
            pst.setString(2,signup.getUserEmail());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                isExist=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //safe to put inside finally
            try {
                if(pst!=null){pst.close();}
                if(con!=null){con.close();}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isExist;
    }

}
