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
public class CertificateGenerator {
    private JsonDatabaseManager db;

    public CertificateGenerator(JsonDatabaseManager db) {
        this.db = db;
    }
    public boolean isEligible(String studentId,String courseId){
        User user=db.getUserById(studentId);
        Course course=db.getCourseById(courseId);
        if(user==null||course==null||!(user instanceof Student))
            return false;
        Student studen=(Student)user;
        List<Lesson> lessons=course.getLessons();
        if(lessons==null||lessons.isEmpty())
            return false;
        for(Lesson lesson:lessons){
            if(!studen.isLessonCompleted(courseId, lesson.getLessonId()))
                return false;
           }
        return true;
       }
    public Certificate generateCertificate(String studentId, String courseId){
        if (isEligible(studentId, courseId)){
            Course course = db.getCourseById(courseId);
            Student student = (Student) db.getUserById(studentId);

            // Check if certificate already exists
            for (Certificate existingCert : student.getCertificates()) {
                if (existingCert.getCourseId().equals(courseId)) {
                    return existingCert;
                }
            }
            String certId = db.generateUniqueId();
            String issueDate = java.time.LocalDate.now().toString();
            Certificate newCert = new Certificate(
                    certId,
                    student.getUsername(),
                    course.getCourseId(),
                    course.getTitle()

            );

            student.addCertificate(newCert);
            db.update(student);
            return newCert;
        }
        return null;
    }
    
    
}
