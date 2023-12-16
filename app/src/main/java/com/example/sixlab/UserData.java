package com.example.sixlab;

public class UserData
{
    private int id_;
    private String login_;
    private String password_;

    public UserData(){}
    public UserData(int id, String login, String password)
    {
        id_ = id;
        login_ = login;
        password_ = password;
    }

    public UserData(String login, String password)
    {
        login_ = login;
        password_ = password;
    }

    public void SetId(int id) {id_ = id;}

    public void SetLogin(String login) {login_ = login;}

    public void SetPassword(String password) {password_ = password;}

    public int GetId() {return id_;}

    public String GetLogin(){return login_;}

    public String GetPassword(){return password_;}
}