package com.example.thirdlab;

public class UserData
{
    private String login_;
    private String password_;

    public UserData(String login, String password) {
        login_ = login;
        password_ = password;
    }

    public void SetLogin(String login) {login_ = login;}

    public void SetPassword(String password) {password_ = password;}

    public String GetLogin(){return login_;}

    public String GetPassword(){return password_;}
}