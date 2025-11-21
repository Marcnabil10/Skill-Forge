package BackEnd;

public class AuthService {
    private final JsonDatabaseManager dbManager ;

    public boolean isUsernameTaken(String username) {
        return dbManager.findUserByUsername(username) != null;
    }
    public boolean isEmailTaken(String email) {
        return dbManager.findUserByEmail(email) != null;
    }

    public AuthService(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }


    public boolean signup(User user) {
        if (isUsernameTaken(user.getUsername()) || isEmailTaken(user.getEmail())) {
            return false;
        }
        String userId = "id" + System.currentTimeMillis();
        user.setUserId(userId);
        user.setPasswordHash(PasswordHasher.hashPassword(user.getPasswordHash()));
        dbManager.save(user);
        return true;
    }

        public  User login(String input, String password) {
            User user = dbManager.findUserByUsername(input);

            if (user == null) {
                user=dbManager.findUserByEmail(input);
                if(user==null)
                    return null;
            }

            if (!PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
                return null;
            }
            return user;
        }




}
