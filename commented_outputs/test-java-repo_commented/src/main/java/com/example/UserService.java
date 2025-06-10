package com.example;

import java.util.ArrayList;
import java.util.List;
import com.example.utils.StringHelper;

public class UserService {
    private List<User> users;
    
    public UserService() {
        this.users = new ArrayList<>();
    }
    
    public void addUser(User user) {
        if (user != null && user.isValidEmail()) {
            users.add(user);
        }
    }
    
    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(StringHelper.normalize(email))) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public boolean deleteUser(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
    
    public int getUserCount() {
        return users.size();
    }
    
    public List<User> searchUsers(String query) {
        List<User> results = new ArrayList<>();
        String normalizedQuery = StringHelper.normalize(query);
        
        for (User user : users) {
            if (user.getName().toLowerCase().contains(normalizedQuery) ||
                user.getEmail().toLowerCase().contains(normalizedQuery)) {
                results.add(user);
            }
        }
        return results;
    }
}