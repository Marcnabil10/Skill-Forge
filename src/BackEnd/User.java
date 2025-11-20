package BackEnd;
import org.json.JSONObject;


    public abstract class User {
        private String userId;
        private String username;
        private String email;
        private String passwordHash;
        private String role;

     public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("username", username);
        json.put("email", email);
        json.put("passwordHash", passwordHash);
        json.put("role", role);
        return json;}


        public User(String userId, String username, String email, String passwordHash, String role) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.passwordHash = passwordHash;
            this.role = role;
        }

        public User() {
        }



        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        
    }


