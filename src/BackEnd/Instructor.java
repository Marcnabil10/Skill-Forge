/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author marcn
 */
public class Instructor extends User {
    private List<String> createdCourses;

    public Instructor(String Username, String UserId, String email, String password, String role) {
        super(Username, UserId, email, password, role);
        this.createdCourses = new ArrayList<>();
    }
    public Instructor(JSONObject obj) {
        super(
            obj.optString("userId"),
            obj.optString("username"),
            obj.optString("email"),
            obj.optString("passwordHash"),
            obj.optString("role")
        );
        if (obj.has("createdCourses")) {
            this.createdCourses = new ArrayList<>(obj.getJSONArray("createdCourses").toList().stream().map(Object::toString).toList());
        } else {
            this.createdCourses = new ArrayList<>();
        }
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }
    
    
    public void addCreatedCourse(String courseId) {
        if (courseId != null && !createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }
    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("createdCourses", this.createdCourses); 
        return json;
    }
    

   

   
}
    
    
   
