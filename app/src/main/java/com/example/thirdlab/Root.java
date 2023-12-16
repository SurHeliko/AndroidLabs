package com.example.thirdlab;

import java.util.List;

public class Root
{
    List<UserData> user;

    public Root(List<UserData> user) {
        this.user = user;
    }

    public List<UserData> GetUser() {
        return user;
    }

    public void SetUser(List<UserData> user) {
        this.user = user;
    }
}