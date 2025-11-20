package BackEnd;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class JsonDatabaseManager {

    private final Path userPath;
    private final Path coursesPath;

    private List<User> usersCache;
    private List<Course> coursesCache;

    public JsonDatabaseManager(String usersFile, String coursesFile) {
        this.userPath = Paths.get(usersFile);
        this.coursesPath = Paths.get(coursesFile);

        ensureFileExists(userPath, "[]");
        ensureFileExists(coursesPath, "[]");

        this.usersCache = loadUsers();
        this.coursesCache = loadCourses();
    }

    private void ensureFileExists(Path p, String defaultContent) {
        try {
            if (!Files.exists(p)) {
                if (p.getParent() != null && !Files.exists(p.getParent())) {
                    Files.createDirectories(p.getParent());
                }
                Files.write(p, defaultContent.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to create database file " + p, e);
        }
    }

    public List<User> loadUsers() {
        try {
            String content = Files.readString(userPath);
            JSONArray arr = new JSONArray(content);
            List<User> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String role = obj.optString("role", "");
                if (role.equalsIgnoreCase("student")) {
                    list.add(new Student(obj));
                } else if (role.equalsIgnoreCase("instructor")) {
                    list.add(new Instructor(obj));
                } else {
                    list.add(new Student(obj)); // fallback
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }

    public List<Course> loadCourses() {
        try {
            String content = Files.readString(coursesPath);
            JSONArray arr = new JSONArray(content);
            List<Course> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(new Course(arr.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("failed to load courses", e);
        }
    }

    public void saveUsers() {
        JSONArray arr = new JSONArray();
        for (User u : usersCache) {
            arr.put(u.toJSON());
        }
        try {
            Files.writeString(userPath, arr.toString(4));
        } catch (Exception e) {
            throw new RuntimeException("failed to save users", e);
        }
    }

    public void saveCourses() {
        JSONArray arr = new JSONArray();
        for (Course c : coursesCache) {
            arr.put(c.toJSON());
        }
        try {
            Files.writeString(coursesPath, arr.toString(4));
        } catch (IOException e) {
            throw new RuntimeException("failed to save courses", e);
        }
    }

    public List<User> getAllUsers() {
        return usersCache;
    }

    public List<Course> getAllCourses() {
        return coursesCache;
    }

    public void addUser(User user) {
        this.usersCache = loadUsers();
        usersCache.add(user);
        saveUsers();
    }

    public boolean saveCourse(Course course) {
        for (Course c : coursesCache) {
            if (c.getCourseId().equals(course.getCourseId())) {
                return false;
            }
        }
        coursesCache.add(course);
        saveCourses();
        return true;
    }

    public boolean updateCourse(Course updatedCourse) {
        for (int i = 0; i < coursesCache.size(); i++) {
            if (coursesCache.get(i).getCourseId().equals(updatedCourse.getCourseId())) {
                coursesCache.set(i, updatedCourse);
                saveCourses();
                return true;
            }
        }
        return false;
    }

    public boolean updateUser(User updatedUser) {
        System.out.println("=== updateUser called ===");
        System.out.println("Updating user ID: " + updatedUser.getUserId());
        System.out.println("User type: " + updatedUser.getClass().getSimpleName());
        
        for (int i = 0; i < usersCache.size(); i++) {
            User currentUser = usersCache.get(i);
            System.out.println("Checking user: " + currentUser.getUserId() + " type: " + currentUser.getClass().getSimpleName());
            
            if (currentUser.getUserId().equals(updatedUser.getUserId())) {
                System.out.println("Found matching user, updating...");
                usersCache.set(i, updatedUser);
                saveUsers();
                System.out.println("User update completed successfully");
                return true;
            }
        }
        System.out.println("=== FAILED: User not found in cache ===");
        return false;
    }

    public User findUserByEmail(String email) {
        for (User u : usersCache) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public Course getCourseById(String id) {
        if (id == null) return null;
        
        for (Course c : coursesCache) {
            if (id.equals(c.getCourseId())) {
                return c;
            }
        }
        return null;
    }

    public boolean deleteCourse(String id) {
        if (id == null) return false;
        
        for (int i = 0; i < coursesCache.size(); i++) {
            if (coursesCache.get(i).getCourseId().equals(id)) {
                coursesCache.remove(i);
                saveCourses();
                return true;
            }
        }
        return false;
    }

    public String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }

    public User getUserById(String id) {
        if (id == null) return null;
        
        for (User u : usersCache) {
            if (u.getUserId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User u : usersCache) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
}
