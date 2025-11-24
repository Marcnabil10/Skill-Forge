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
     public static final String STATUS_PENDING="PENDING";
     public static final String STATUS_APPROVED="APPROVED";
      public static final String STATUS_REJECTED="REJECTED";
    
    private String courseId;
    private String title;
    private String description;
    private String instructor;
    private List <Lesson> lessons;
    private List <String> students;
    private String status;
   

   public Course(String courseId, String title, String description, String instructor) {
    this.courseId = courseId;  
    this.title = title;
    this.description = description;
    this.instructor = instructor;
    this.lessons = new ArrayList<>();
    this.students = new ArrayList<>();
    this.status=STATUS_PENDING;//byebda2 el course pending
   
}
    public Course(JSONObject jsonObject) {
        this.courseId = jsonObject.optString("courseId");
        this.title = jsonObject.optString("title");
        this.description = jsonObject.optString("description");
        this.instructor = jsonObject.optString("instructor");
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
        if (jsonObject.has("students")) {
            this.students.addAll(jsonObject.getJSONArray("students").toList().stream().map(Object::toString).toList());
        }
        String status=jsonObject.optString("status", STATUS_PENDING);
        if(status.equals(STATUS_APPROVED)||status.equals(STATUS_REJECTED)){
            this.status=status;
        }
        else{
            this.status=STATUS_PENDING;
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
                if (lessonJson.has("quiz")) {
                    JSONObject q = lessonJson.getJSONObject("quiz");
                    Quiz quiz = new Quiz(q.getString("title"), q.getInt("attempts"));

                    JSONArray qs = q.getJSONArray("questions");
                    for (int j = 0; j < qs.length(); j++) {
                        quiz.addQuestion(new Question(qs.getJSONObject(j)));
                    }

                    JSONObject at = q.getJSONObject("studentAttempts");
                    for (String key : at.keySet()) {
                        quiz.getStudentAttempts().put(key, at.getInt(key));
                    }
                    if (q.has("studentScores")) {
                        JSONObject ss = q.getJSONObject("studentScores");
                        for (String key : ss.keySet()) {
                            quiz.getStudentScores().put(key, ss.getDouble(key));
                        }
                    }


                    lesson.setQuiz(quiz);
                }

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

    public String getInstructor() {
        return instructor;
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
        json.put("instructor", instructor);
        json.put("status", this.status);
        
        JSONArray lessonsArray = new JSONArray();
        for (Lesson lesson : this.lessons) {
            JSONObject lessonJson = new JSONObject();
            lessonJson.put("lessonId", lesson.getLessonId());
            lessonJson.put("title", lesson.getTitle());
            lessonJson.put("content", lesson.getContent());
            if (lesson.getQuiz() != null) {
                Quiz q = lesson.getQuiz();
                JSONObject quizJson = new JSONObject();
                quizJson.put("id", q.getId());
                quizJson.put("title", q.getTitle());
                quizJson.put("attempts", q.getAttempts());
                JSONArray qArr = new JSONArray();
                for (Question qu : q.getQuestions()) {
                    qArr.put(qu.toJSON());
                }
                quizJson.put("questions", qArr);
                quizJson.put("studentAttempts", new JSONObject(q.getStudentAttempts()));
                quizJson.put("studentScores", new JSONObject(q.getStudentScores()));
                lessonJson.put("quiz", quizJson);
            }




            lessonJson.put("resources", new JSONArray(lesson.getResources())); 
            
            lessonsArray.put(lessonJson);
        }

        json.put("lessons", lessonsArray);
        json.put("students", new JSONArray(this.students));

        return json;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    
 
  
}
