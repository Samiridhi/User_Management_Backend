package org.example.userapi.controller;

import org.example.userapi.model.User;

import java.util.List;


public class UserResponse {
    private List<User> users;

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}
