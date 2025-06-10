import java.util.Date;

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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public boolean isValidEmail() {
        return email != null && email.contains("@");
    }
    
    public String getDisplayName() {
        if (name == null || name.trim().isEmpty()) {
            return email;
        }
        return name;
    }
}