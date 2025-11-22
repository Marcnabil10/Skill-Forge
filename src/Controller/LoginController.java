package Controller;

import BackEnd.AuthService;
import BackEnd.ValidationService;
import BackEnd.JsonDatabaseManager;
import BackEnd.User;
import BackEnd.*;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    AuthService auth;
    private final JsonDatabaseManager dbManager;
    private final StudentService studentService;
    private final InstructorService instructorService;
    private final AdminService adminService;
    private Student currentStudent;
    private Course currentCourse;
    private Lesson currentLesson;

    public LoginController() {
        this.dbManager = new JsonDatabaseManager("users.json", "courses.json");
        this.auth = new AuthService(dbManager);
        this.studentService = new StudentService(dbManager);
        this.instructorService = new InstructorService(dbManager);
        this.adminService = new AdminService(dbManager);

    }

    public void setCurrentSession(Student student, Course course, Lesson lesson) {
        this.currentStudent = student;
        this.currentCourse = course;
        this.currentLesson = lesson;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

    public Lesson getCurrentLesson() {
        return currentLesson;
    }

    public User login(String input, String password) {
        if (!ValidationService.isEmailValid(input) && !ValidationService.isUsernameValid(input)) {
            return null;
        }
        return auth.login(input, password);
    }

    public Student getStudent(User user) {
        if (user instanceof Student) {
            return (Student) user;
        }
        return null;
    }

    public Instructor getInstructor(User user) {
        if (user instanceof Instructor) {
            return (Instructor) user;
        }
        return null;
    }

    public Admin getAdmin(User user) {
        if (user instanceof Admin) {
            return (Admin) user;
        }
        return null;
    }

    public ArrayList<Course> getAllCourses() {
        return new ArrayList<>(dbManager.getAllCourses());
    }

    public Course getCourseById(String courseId) {
        return dbManager.getCourseById(courseId);
    }

    public User getUserByName(String name) {
        return dbManager.findUserByUsername(name);
    }

    public boolean enrollInCourse(Student student, Course course) {
        return studentService.enrollInCourse(student, course);
    }

    public ArrayList<Course> getEnrolledCourses(Student student) {
        return studentService.getEnrolledCourses(student);
    }

    public boolean createCourse(String title, String description, Instructor instructor) {
        return instructorService.createCourse(title, description, instructor);
    }

    public boolean editCourse(String courseId, String newTitle, String newDescription) {
        return instructorService.editCourse(courseId, newTitle, newDescription);
    }

    public boolean deleteCourse(String courseId, Instructor instructor) {
        return instructorService.deleteCourse(courseId, instructor);
    }

    public ArrayList<Student> getEnrolledStudents(String courseId) {
        return new ArrayList<>(instructorService.getEnrolledStudents(courseId));
    }

    public List<Course> getPendingCourses() {
        return adminService.getPendingCourses();
    }

    public boolean updateCourseStatus(String courseId, String newStatus) {
        return adminService.updateCourseStatus(courseId, newStatus);
    }

    public boolean approveCourse(String courseId) {
        return updateCourseStatus(courseId, Course.STATUS_APPROVED);
    }

    public boolean rejectCourse(String courseId) {
        return updateCourseStatus(courseId, Course.STATUS_REJECTED);
    }

    public boolean addLesson(String courseId, String title, String content) {
        return instructorService.addLesson(courseId, title, content);
    }

    public boolean editLesson(String courseId, String lessonId, String newTitle, String newContent) {
        return instructorService.editLesson(courseId, lessonId, newTitle, newContent);
    }

    public boolean deleteLesson(String courseId, String lessonId) {
        return instructorService.deleteLesson(courseId, lessonId);
    }

    public ArrayList<Course> getMyCourses(String instructorId) {
        return new ArrayList<>(instructorService.getMyCourses(instructorId));
    }

    public boolean markLessonCompleted(Student student, Course course, Lesson lesson) {
        return studentService.markLessonCompleted(student, course, lesson);
    }

    public boolean isLessonCompleted(Student student, Course course, Lesson lesson) {
        return studentService.isLessonCompleted(student, course, lesson);
    }

    public ArrayList<String> getCompletedLessons(Student student, Course course) {
        return studentService.getCompletedLessons(student, course);
    }

    public double getCourseProgress(Student student, Course course) {
        return studentService.getCourseProgress(student, course);
    }

    public User getUserByUsername(String username) {
        return dbManager.findUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return dbManager.findUserByEmail(email);
    }

    public boolean saveUser(User user) {
        return dbManager.save(user);
    }

    public String generateUniqueId() {
        return dbManager.generateUniqueId();
    }

    public boolean updateCourse(Course course) {
        return dbManager.update(course);
    }

    public boolean updateUser(User user) {
        return dbManager.update(user);
    }

    public Quiz getCurrentQuiz() {
        if (currentLesson != null) {
            return currentLesson.getQuiz();
        }
        return null;
    }

    public String getCurrentLessonTitle() {
        if (currentLesson != null) {
            return currentLesson.getTitle();
        } else {
            return "Unknown Lesson";
        }
    }

    public int getQuizQuestionCount() {
        if (currentLesson == null) {
            return 0;
        }

        Quiz quiz = currentLesson.getQuiz();
        if (quiz == null) {
            return 0;
        }

        return quiz.getQuestions().size();
    }

    public String getQuestionText(int index) {
        Quiz quiz = getCurrentQuiz();
        if (quiz == null) {
            return "Error: No quiz found";
        }

        if (index < 0 || index >= quiz.getQuestions().size()) {
            return "Error: Index out of bounds";
        }

        return quiz.getQuestions().get(index).getQuestionText();
    }

    public List<String> getQuestionOptions(int index) {
        Quiz quiz = getCurrentQuiz();
        if (quiz == null) {
            return new ArrayList<>();
        }
        return quiz.getQuestions().get(index).getOptions();
    }

    public String submitCurrentQuiz(List<Integer> answers) {
        Quiz quiz = getCurrentQuiz();

        if (quiz == null) {
            return "Error: No quiz active";
        }
        QuizResult result = quiz.finish(currentStudent.getUsername(), answers);

        dbManager.update(currentCourse);

        String status;
        if (result.isPassed()) {
            status = "PASSED";
        } else {
            status = "FAILED";
        }

        return String.format("Score: %.1f%%\nStatus: %s", result.getScore(), status);
    }

}
