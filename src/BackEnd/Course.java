/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author marcn
 */
public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private List <Lesson> lessons;
    private List <String> students;

   public Course(String courseId, String title, String description, String instructorId) {
    this.courseId = courseId;  
    this.title = title;
    this.description = description;
    this.instructorId = instructorId;
    this.lessons = new ArrayList<>();
    this.students = new ArrayList<>();
}
    public Course(JSONObject jsonObject) {
        this.courseId = jsonObject.optString("courseId");
        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.instructorId = jsonObject.optString("instructorId");
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
        if (jsonObject.has("students")) {
            this.students.addAll(jsonObject.getJSONArray("students").toList().stream().map(Object::toString).toList());
        }
        if (jsonObject.has("lessons")) {
            JSONArray lessonsArray = jsonObject.getJSONArray("lessons");
            for (int i = 0; i < lessonsArray.length(); i++) {
                JSONObject lessonJson = lessonsArray.getJSONObject(i);
                Lesson lesson = new Lesson(
                    lessonJson.optString("lessonId"),
                    lessonJson.optString("title"),
                    lessonJson.optString("content")
                );
                this.lessons.add(lesson);
            }
        }
    }


    public String getCourseId() {
        return courseId;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<String> getStudents() {
        return students;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<String> getStudentsID() {
        return students;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
    }
    public void removeLesson(String lessonId){
        Lesson lessontoRemove=null;
       for(Lesson lesson:this.lessons) {
           if(lesson.getLessonId().equals(lessonId)){
               lessontoRemove=lesson;
               break;
           }
           }
       if(lessontoRemove!=null){
           this.lessons.remove(lessontoRemove);
       }
    }
    public void addStudent(String studentId) {
        if (!this.students.contains(studentId)) {
            this.students.add(studentId);
        }
    }
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("courseId", courseId);
        json.put("title", title);
        json.put("description", description);
        json.put("instructorId", instructorId);
        
        JSONArray lessonsArray = new JSONArray();
        for (Lesson lesson : this.lessons) {
            JSONObject lessonJson = new JSONObject();
            lessonJson.put("lessonId", lesson.getLessonId());
            lessonJson.put("title", lesson.getTitle());
            lessonJson.put("content", lesson.getContent());
            
          
            lessonJson.put("resources", new JSONArray(lesson.getResources())); 
            
            lessonsArray.put(lessonJson);
        }
        json.put("lessons", lessonsArray);
        json.put("students", new JSONArray(this.students));
        
        return json;
    }
    
    
    
}
