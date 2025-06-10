import java.util.Date;

/** 
 * Represents a user with a name, email, and creation date. 
 * Provides methods for accessing and validating user information. 
 */

public class User {
    private String name;
    private String email;
    private Date createdAt;
    
    public User() {
        this.createdAt = new Date();
    }
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.createdAt = new Date();
    }
    
/**
 * Retrieves name.
 *
 * @return Name of the entity
 */

    public String getName() {
        return name;
    }
    
/**
 * Sets user's name.
 *
 * @param name User's name
 */

    public void setName(String name) {
        this.name = name;
    }
    
/**
 * Retrieves email address.
 *
 * @return Email address
 */

    public String getEmail() {
        return email;
    }
    
/**
 * Sets user's email.
 *
 * @param email User's email
 */

    public void setEmail(String email) {
        this.email = email;
    }
    
/**
 * Retrieves creation date.
 *
 * @return Creation date
 */

    public Date getCreatedAt() {
        return createdAt;
    }
    
/**
 * Checks if email is valid.
 *
 * @return True if valid
 */

    public boolean isValidEmail() {
        return email != null && email.contains("@");
    }
    
/**
 * Retrieves display name, prioritizing name over email.
 *
 * @return Display name or email if name is empty
 */

    public String getDisplayName() {
        if (name == null || name.trim().isEmpty()) {
            return email;
        }
        return name;
    }
}