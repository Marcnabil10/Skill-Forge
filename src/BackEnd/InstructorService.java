package BackEnd;

import java.util.ArrayList;
import java.util.List;

public class InstructorService {
    private JsonDatabaseManager dbManager;

    public InstructorService(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean createCourse(String title, String description, Instructor instructor) {
        System.out.println("=== Starting createCourse ===");
        System.out.println("Title: " + title + ", Instructor : " + instructor.getUsername());
        
        String courseId = dbManager.generateUniqueId();
        System.out.println("Generated course ID: " + courseId);
        
        Course newCourse = new Course(courseId, title, description, instructor.getUsername());
        System.out.println("Course object created: " + newCourse.getTitle());
        
        boolean saveSuccess = dbManager.save(newCourse);
        System.out.println("Database save success: " + saveSuccess);
        
        if (saveSuccess) {
            System.out.println("=== Course creation COMPLETE ===");
            return true;
        } else {
            System.out.println("=== FAILED: Could not save course to database ===");
        }
        return false;
    }

    public boolean addLesson(String courseId, String lessonTitle, String lessonContent) {
        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found: " + courseId);
            return false;
        }
        
        String lessonId = dbManager.generateUniqueId();
        Lesson newLesson = new Lesson(lessonId, lessonTitle, lessonContent);
        
        
        if (course.getLessons() == null) {
            course.setLessons(new ArrayList<>());
        }
        
        course.getLessons().add(newLesson);
        boolean success = dbManager.update(course);
        System.out.println("Lesson added: " + success);
        return success;
    }

    public boolean editCourse(String courseId, String newTitle, String newDescription) {
        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found: " + courseId);
            return false;
        }
        
        course.setTitle(newTitle);
        course.setDescription(newDescription);
        
        boolean success = dbManager.update(course);
        System.out.println("Course updated: " + success);
        return success;
    }

    public boolean deleteCourse(String courseId, Instructor instructor) {
        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found: " + courseId);
            return false;
        }
       
        if (!course.getInstructor().equals(instructor.getUserId())) {
            System.out.println("Instructor does not own this course");
            return false;
        }
        
        boolean deleted = dbManager.deleteCourse(courseId);
        System.out.println("Course deleted: " + deleted);
        return deleted;
    }

    public boolean editLesson(String courseId, String lessonId, String newTitle, String newContent) {
        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found: " + courseId);
            return false;
        }
        
        List<Lesson> lessons = course.getLessons();
        if (lessons == null) {
            System.out.println("No lessons found in course");
            return false;
        }
        
        for (Lesson lesson : lessons) {
            if (lesson.getLessonId().equals(lessonId)) {
                lesson.setTitle(newTitle);
                lesson.setContent(newContent);
                boolean success = dbManager.update(course);
                System.out.println("Lesson updated: " + success);
                return success;
            }
        }
        
        System.out.println("Lesson not found: " + lessonId);
        return false;
    }

    public boolean deleteLesson(String courseId, String lessonId) {
        Course course = dbManager.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found: " + courseId);
            return false;
        }
        
        List<Lesson> lessons = course.getLessons();
        if (lessons == null) {
            System.out.println("No lessons found in course");
            return false;
        }
        
        boolean found = false;
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(lessonId)) {
                lessons.remove(i);
                found = true;
                break;
            }
        }
        
        if (found) {
            boolean success = dbManager.update(course);
            System.out.println("Lesson deleted: " + success);
            return success;
        }
        
        System.out.println("Lesson not found: " + lessonId);
        return false;
    }

    public List<Course> getMyCourses(String instructorId) {
        List<Course> allCourses = dbManager.getAllCourses();
        List<Course> myCourses = new ArrayList<>();
        
        for (Course course : allCourses) {
            if (course.getInstructor().equals(instructorId)) {
                myCourses.add(course);
            }
        }
        
        System.out.println("Found " + myCourses.size() + " courses for instructor: " + instructorId);
        return myCourses;
    }

    public List<Student> getEnrolledStudents(String courseId) {
        List<Student> enrolledStudents = new ArrayList<>();
        Course course = dbManager.getCourseById(courseId);
        
        if (course != null) {
            List<String> studentIds = course.getStudents();
            System.out.println("Found " + studentIds.size() + " enrolled students for course: " + courseId);
            
            for (String id : studentIds) {
                User user = dbManager.getUserById(id);
                if (user instanceof Student) {
                    enrolledStudents.add((Student) user);
                }
            }
        } else {
            System.out.println("Course not found: " + courseId);
        }
        
        return enrolledStudents;
    }
}
