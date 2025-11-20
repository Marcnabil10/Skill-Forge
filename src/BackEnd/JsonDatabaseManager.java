package BackEnd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonDatabaseManager implements DataBase { 

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
            throw new RuntimeException("Failed to create database file " + p, e);
        }
    }

    private List<User> loadUsers() {
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
                } else if (role.equalsIgnoreCase("admin")) { 
                    list.add(new Admin(obj));
                } else {
                    list.add(new Student(obj)); 
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }

    private List<Course> loadCourses() {
        try {
            String content = Files.readString(coursesPath);
            JSONArray arr = new JSONArray(content);
            List<Course> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(new Course(arr.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load courses", e);
        }
    }


    private void saveUsersToFile() {
        JSONArray arr = new JSONArray();
        for (User u : usersCache) {
            arr.put(u.toJSON());
        }
        try {
            Files.writeString(userPath, arr.toString(4));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save users", e);
        }
    }

    private void saveCoursesToFile() {
        JSONArray arr = new JSONArray();
        for (Course c : coursesCache) {
            arr.put(c.toJSON());
        }
        try {
            Files.writeString(coursesPath, arr.toString(4));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save courses", e);
        }
    }


    @Override
   public boolean save(User user) {
        this.usersCache = loadUsers(); 
        usersCache.add(user);
        saveUsersToFile();
        return true;
    }

    @Override
    public boolean save(Course course) {
        for (Course c : coursesCache) {
            if (c.getCourseId().equals(course.getCourseId())) {
                return false; 
            }
        }
        coursesCache.add(course);
        saveCoursesToFile();
        return true;
    }

    @Override
    public boolean update(User updatedUser) {
        for (int i = 0; i < usersCache.size(); i++) {
            User currentUser = usersCache.get(i);
            if (currentUser.getUserId().equals(updatedUser.getUserId())) {
                usersCache.set(i, updatedUser);
                saveUsersToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Course updatedCourse) {
        for (int i = 0; i < coursesCache.size(); i++) {
            if (coursesCache.get(i).getCourseId().equals(updatedCourse.getCourseId())) {
                coursesCache.set(i, updatedCourse);
                saveCoursesToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteCourse(String id) {
        if (id == null) return false;
        for (int i = 0; i < coursesCache.size(); i++) {
            if (coursesCache.get(i).getCourseId().equals(id)) {
                coursesCache.remove(i);
                saveCoursesToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public User getUserById(String id) {
        if (id == null) return null;
        for (User u : usersCache) {
            if (u.getUserId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public Course getCourseById(String id) {
        if (id == null) return null;
        for (Course c : coursesCache) {
            if (id.equals(c.getCourseId())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return usersCache;
    }

    @Override
    public List<Course> getAllCourses() {
        return coursesCache;
    }

    @Override
    public User findUserByUsername(String username) {
        for (User u : usersCache) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        for (User u : usersCache) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
    public String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }
}