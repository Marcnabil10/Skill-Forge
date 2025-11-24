package BackEnd;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class StudentService {
    private JsonDatabaseManager dbManager;

    public StudentService(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public ArrayList<Course> getAllCourses() {
        return (ArrayList<Course>) dbManager.getAllCourses();
    }

    public Course getCourseById(String courseId) {
        return dbManager.getCourseById(courseId);
    }
    
    public boolean enrollInCourse(Student student, Course course) {
       JOptionPane.showMessageDialog(null, "Enrolling student " + student.getUsername() + " in course " + course.getTitle());
        
        if (!isStudentEnrolled(student, course.getCourseId())) {
            student.addEnrolledCourse(course);

            course.addStudent(student.getUserId());

            boolean studentSaved = dbManager.update(student);
            boolean courseSaved = dbManager.update(course);
            
            if (studentSaved && courseSaved) {
                JOptionPane.showMessageDialog(null, "Successfully enrolled student in course");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to save enrollment in database");
               
                student.getEnrolledCourses().remove(course);
                course.getStudents().remove(student.getUserId());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "You are already enrolled in this course");
            return false;
        }
    }
    
    public boolean isStudentEnrolled(Student student, String courseId) {
        if (student == null || courseId == null) {
            return false;
        }
        return student.isEnrolledInCourse(courseId);
    }
    
    public ArrayList<Course> getEnrolledCourses(Student student) {
        if (student == null) {
            return new ArrayList<>();
        }
        return student.getEnrolledCourses();
    }

    public boolean markLessonCompleted(Student student, Course course, Lesson lesson) {
        if (student == null || course == null || lesson == null) return false;

        Quiz quiz = lesson.getQuiz();
        if (quiz != null) {
            Double score = quiz.getScore(student.getUsername());
            if (score == null || !quiz.isPassed(score)) {
                return false;
            }
        }

        student.markLessonCompleted(course.getCourseId(), lesson.getLessonId());
        return dbManager.update(student);
    }



    public boolean isLessonCompleted(Student student, Course course, Lesson lesson) {
        if (student == null || course == null || lesson == null) {
            return false;
        }
        if (lesson.getQuiz() != null) {
             Double score = lesson.getQuiz().getScore(student.getUsername());
            if (score == null) {
        return false; 
    }
         if (!lesson.getQuiz().isPassed(score)) {
            return false;
            }
        }

        boolean completed = student.isLessonCompleted(course.getCourseId(), lesson.getLessonId());
        return completed;
    }
    
    public ArrayList<String> getCompletedLessons(Student student, Course course) {
        if (student == null || course == null) {
            return new ArrayList<>();
        }
        
      
        return student.getCompletedLessons(course.getCourseId());
    }
    
    public double getCourseProgress(Student student, Course course) {
        if (student == null || course == null) {
            return 0.0;
        }
        
        double totalLessons = course.getLessons().size();
        double doneLessons = getCompletedLessons(student, course).size();
        
        JOptionPane.showMessageDialog(null, "Progress calculation - Total: " + totalLessons + ", Done: " + doneLessons);
        
        if (totalLessons == 0) {
            return 0.0;
        } else {
            double progress = (doneLessons / totalLessons) * 100;
            JOptionPane.showMessageDialog(null, "Progress: " + progress + "%");
            return progress;
        }
    }
    public boolean canAccessLesson(Student student, Course course, Lesson lesson) {
        if (student == null || course == null || lesson == null) {
            return false;
        }
        List<Lesson> lessons = course.getLessons();
        int currentIndex = lessons.indexOf(lesson);
        if (currentIndex == 0) {
            return true;
        }
        for (int i = 0; i < currentIndex; i++) {
            Lesson previousLesson = lessons.get(i);
            if (!isLessonCompleted(student, course, previousLesson)) {
                return false;
            }
        }return true;
    }}
