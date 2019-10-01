
package com.movil.utils;

import com.movil.models.user;


public class Usregistrationrequest {
    private user newuser;
    private String pwd;
    private String pwdConfirmation;

    public Usregistrationrequest(user newUser, String pwd, String pwdConfirmation) {
        this.newuser = newUser;
        this.pwd = pwd;
        this.pwdConfirmation = pwdConfirmation;
    }

    public user getNewUser() {
        return newuser;
    }

    public void setNewUser(user newUser) {
        this.newuser = newUser;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwdConfirmation() {
        return pwdConfirmation;
    }

    public void setPwdConfirmation(String pwdConfirmation) {
        this.pwdConfirmation = pwdConfirmation;
    }
}
