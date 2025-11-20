/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

import java.util.List;

/**
 *
 * @author marcn
 */
public interface DataBase {
    boolean save(User user);
    boolean save(Course course);
    boolean update(User user);
    boolean update(Course course);
    boolean deleteCourse(String courseId);
    User getUserById(String id);
    Course getCourseById(String id);
    List<User> getAllUsers();
    List<Course> getAllCourses();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
     
}
