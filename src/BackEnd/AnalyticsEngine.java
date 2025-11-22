package BackEnd;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnalyticsEngine {

    private Course course;
    private Lesson lesson;
    private InstructorService instructorService;
    private StudentService studentService;
    public AnalyticsEngine(Course course , Lesson lesson,InstructorService instructorService, StudentService studentService){
        this.studentService=studentService;
        this.course=course;
        this.lesson=lesson;
        this.instructorService=instructorService;

    }
    public double averageScore(){
        if (lesson == null || lesson.getQuiz() == null || lesson.getQuiz().getStudentScores() == null) {
            return 0.0;
        }
        List<Double> scores =  new ArrayList<>(lesson.getQuiz().getStudentScores().values());
        double total=0.0;
        if(scores.size()==0)
            return 0.0;
        for(Double s:scores){
            total+=s;
        }
        return total/scores.size();
    }
    public double calculateCompletionPercentage(Course course){
        List<Student> students=instructorService.getEnrolledStudents(course.getCourseId());
        double progressTotal=0.0;
        if(students.size()==0)
            return 0.0;
        for(Student s:students){
            progressTotal +=studentService.getCourseProgress(s,course);
        }
        return progressTotal/students.size();

    }
    public double averageStudentQuizScorePer(Course course, Student student){
        if(course == null || course.getLessons() == null)
            return 0.0;;
        List<Lesson> lessons=course.getLessons();
        double average=0.0;
        int numberOfTakenQuiz=0;;
        for(Lesson l:lessons){
            if(l.getQuiz()!=null && l.getQuiz().getScore(student.getUsername())!=null){
                average+=l.getQuiz().getScore(student.getUsername());
                numberOfTakenQuiz++;}

        }
        if(numberOfTakenQuiz==0)
            return 0.0;
        return average/numberOfTakenQuiz ;

    }

}
