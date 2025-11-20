/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcn
 */
public class AdminService {
    private JsonDatabaseManager db;

    public AdminService(JsonDatabaseManager db) {
        this.db = db;
    }
    public List<Course> getPendingCourses(){
        List<Course> allCourses=db.getAllCourses();
        List<Course> pendingCourses=new ArrayList<>();
        for(Course course:allCourses){
            if(course.getStatus().equals(Course.STATUS_PENDING))
                pendingCourses.add(course);
        }
        return pendingCourses;
       }
  private boolean updateCourseStatus(String courseId,String newStatus){
      Course course=db.getCourseById(courseId);
      if(course==null)
          return false;
      course.setStatus(newStatus);
      return db.update(course);
  }
  public boolean approveCourse(String courseId){
      return updateCourseStatus(courseId,Course.STATUS_APPROVED);
  }
   public boolean rejectCourse(String courseId){
      return updateCourseStatus(courseId,Course.STATUS_REJECTED);
  }
}
