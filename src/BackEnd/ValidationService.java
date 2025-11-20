package BackEnd;

public class ValidationService {
    public static boolean isEmailValid(String email) {
        if(email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf("@");
        if(atIndex < 1 || atIndex != email.lastIndexOf("@")) {
            return false;
        }

        String domain = email.substring(atIndex + 1);
        if(!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }

        return true;
    }

    public static boolean isUsernameValid(String username) {
        if(username == null || username.isEmpty()) {
            return false;
        }
        if(username.contains(" ")) {
            return false;
        }
        return true;
    }
    public static boolean isRoleValid(String role){
        if(role.equalsIgnoreCase("student")|| role.equalsIgnoreCase("instructor"))
            return true;
        else return false;
    }
    public static boolean isPasswordValid(String password) {
    if (password == null) {
        return false;
    }
    return password.length() >= 6;
}


}
