package com.example;

import java.util.ArrayList;
import java.util.List;
import com.example.utils.StringHelper;

/**
 * The UserService class manages a collection of User objects, providing methods to add, find, delete, and retrieve users. 
 * It ensures that only users with valid email addresses are added to the collection.
 */

public class UserService {
    private List<User> users;
    
    public UserService() {
        this.users = new ArrayList<>();
    }
    
/**
 * Adds a user to the list if valid.
 *
 * @param user User to add
 */

    public void addUser(User user) {
        if (user != null && user.isValidEmail()) {
            users.add(user);
        }
    }
    
/**
 * Finds a user by email.
 *
 * @param email Email of the user
 * @return User object if found, null otherwise
 */

    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(StringHelper.normalize(email))) {
                return user;
            }
        }
        return null;
    }
    
/**
 * Retrieves all users.
 *
 * @return List of users
 */

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
/**
 * Deletes a user by email.
 *
 * @param email Email of the user
 * @return True if deleted
 */

    public boolean deleteUser(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
    
/**
 * Retrieves count of users.
 *
 * @return Count of users
 */

    public int getUserCount() {
        return users.size();
    }
    
/**
 * Searches for users by name or email.
 *
 * @param query Search term for user name or email
 * @return List of users matching search criteria
 */

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