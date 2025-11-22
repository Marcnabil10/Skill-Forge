package BackEnd;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User {
    private ArrayList<Course> enrolledCourses;
    private HashMap<String, ArrayList<String>> progress; 
    private ArrayList<Certificate> certificates;

    public Student(String userId, String username, String email, String passwordHash, String role) {
        super(userId, username, email, passwordHash, role);
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.certificates = new ArrayList<>();
    }

    public Student(JSONObject obj) {
        super(
            obj.optString("userId"),
            obj.optString("username"),
            obj.optString("email"),
            obj.optString("passwordHash"),
            obj.optString("role")
                
        );
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.certificates = new ArrayList<>();
        if (obj.has("certificates")) {
    JSONArray certsArray = obj.getJSONArray("certificates");
    for (int i = 0; i < certsArray.length(); i++) {
        JSONObject certJson = certsArray.getJSONObject(i);
        Certificate cert = new Certificate(certJson);
        this.certificates.add(cert);
    }
        }
        
        
        if (obj.has("enrolledCourses")) {
            JSONArray coursesArray = obj.getJSONArray("enrolledCourses");
            for (int i = 0; i < coursesArray.length(); i++) {
                JSONObject courseJson = coursesArray.getJSONObject(i);
                Course course = new Course(courseJson);
                this.enrolledCourses.add(course);
            }
        }
        
      
        if (obj.has("progress")) {
            JSONObject progressJson = obj.getJSONObject("progress");
            for (String courseId : progressJson.keySet()) {
                JSONArray lessonsArray = progressJson.getJSONArray(courseId);
                ArrayList<String> completedLessons = new ArrayList<>();
                for (int i = 0; i < lessonsArray.length(); i++) {
                    completedLessons.add(lessonsArray.getString(i));
                }
                this.progress.put(courseId, completedLessons);
            }
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        JSONArray certsArray = new JSONArray();
for (Certificate cert : this.certificates) {
    certsArray.put(cert.toJSON());
}
json.put("certificates", certsArray);
      
        JSONArray coursesArray = new JSONArray();
        for (Course course : this.enrolledCourses) {
            coursesArray.put(course.toJSON());
        }
        json.put("enrolledCourses", coursesArray);
        
     
        JSONObject progressJson = new JSONObject();
        for (String courseId : this.progress.keySet()) {
            progressJson.put(courseId, new JSONArray(this.progress.get(courseId)));
        }
        json.put("progress", progressJson);
        
        return json;
    }
    
    public ArrayList<Course> getEnrolledCourses() {
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        if (enrolledCourses != null) {
            this.enrolledCourses = enrolledCourses;
        } else {
            this.enrolledCourses = new ArrayList<>();
        }
    }

    public HashMap<String, ArrayList<String>> getProgress() {
        if (progress == null) {
            progress = new HashMap<>();
        }
        return progress;
    }

    public void setProgress(HashMap<String, ArrayList<String>> progress) {
        if (progress != null) {
            this.progress = progress;
        } else {
            this.progress = new HashMap<>();
        }
    }
    
  
    public void addEnrolledCourse(Course course) {
        if (course != null && !isEnrolledInCourse(course.getCourseId())) {
            this.enrolledCourses.add(course);
        }
    }
    

    public boolean isEnrolledInCourse(String courseId) {
        for (Course course : getEnrolledCourses()) {
            if (course.getCourseId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }
    
  
    public void markLessonCompleted(String courseId, String lessonId) {
        if (!progress.containsKey(courseId)) {
            progress.put(courseId, new ArrayList<>());
        }
        ArrayList<String> completedLessons = progress.get(courseId);
        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
        }
    }
    

    public boolean isLessonCompleted(String courseId, String lessonId) {
        return progress.containsKey(courseId) && progress.get(courseId).contains(lessonId);
    }
    

    public ArrayList<String> getCompletedLessons(String courseId) {
        return progress.getOrDefault(courseId, new ArrayList<>());
    }
    
    public ArrayList<Certificate> getCertificates() {
        return certificates;
    }

     public void addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        }
    }
