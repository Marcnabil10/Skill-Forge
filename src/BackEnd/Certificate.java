package BackEnd;

import java.time.LocalDate;
import org.json.JSONObject;

public class Certificate {
    private String certificateId;
    private String studentId;
    private String courseId;
    private String courseName;
    private String issueDate;
    
    public Certificate(String certificateId, String studentId, String courseId, String courseName) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.issueDate = LocalDate.now().toString();// for the date
    }

    public Certificate(JSONObject obj) {
        this.certificateId = obj.optString("certificateId");
        this.studentId = obj.optString("studentId");
        this.courseId = obj.optString("courseId");
        this.courseName = obj.optString("courseName");
        this.issueDate = obj.optString("issueDate");
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("certificateId", certificateId);
        json.put("studentId", studentId);
        json.put("courseId", courseId);
        json.put("courseName", courseName);
        json.put("issueDate", issueDate);
        return json;
    }

    public String getCertificateId() { return certificateId; }
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getIssueDate() { return issueDate; }

}