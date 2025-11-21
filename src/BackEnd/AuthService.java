package BackEnd;

public class AuthService {
    private static final JsonDatabaseManager dbManager = new JsonDatabaseManager("users.json", "courses.json");

    public static boolean isUsernameTaken(String username) {
        return dbManager.findUserByUsername(username) != null;
    }
    public static boolean isEmailTaken(String email) {
        return dbManager.findUserByEmail(email) != null;
    }
    public static boolean signup(User user) {
        if (isUsernameTaken(user.getUsername()) || isEmailTaken(user.getEmail())) {
            return false;
        }
        String userId = "id" + System.currentTimeMillis();
        user.setUserId(userId);
        user.setPasswordHash(PasswordHasher.hashPassword(user.getPasswordHash()));
        dbManager.save(user);
        return true;
    }

        public static User login(String username, String password) {
            User user = dbManager.findUserByUsername(username);
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
