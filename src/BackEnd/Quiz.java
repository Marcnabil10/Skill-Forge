package BackEnd;
import java.util.*;
import java.util.ArrayList;

public class Quiz {
    private String title;
    private List<Question>questions = new ArrayList<>();
    public static final double DEFAULT_PASSING_PERCENTAGE = 50.0;
    public int attempts;
    private String id;
    private Map<String, Integer> studentAttempts = new HashMap<>();
    private Map<String, Double> studentScores = new HashMap<>();

    public Quiz(String title,int attempts){
        if(attempts<1) attempts=1;
        this.attempts=attempts;;
        this.title= title;
        id = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        if(attempts<1) attempts=1;;
        this.attempts = attempts;
    }

    public String getId() {
        return id;
    }
    public void addQuestion(Question q){
        questions.add(q);
    }

    public Boolean deleteQuestion(Question q){
        return  questions.remove(q);
    }
    public Boolean deleteQuestion(int index){
        if(index >= 0 && index < questions.size()){
            questions.remove(index);
            return true;
        } else {
            return false;
        }
    }
    public Question getQuestion(int index){
        if(index >= 0 && index < questions.size()){
            return questions.get(index);
        } else {
            return null;
        }
    }

    public boolean editQuestion(int index, Question newQuestion){
        if(index >= 0 && index < questions.size()){
            questions.set(index,newQuestion);
            return true;
        } else {
            return false;
        }

    }
    public boolean isPassed(double score){
        return score>=DEFAULT_PASSING_PERCENTAGE;
    }

    public double calculateScore(List<Integer> studentAnswers) {
        int correctAnswers = 0;
        for(int i = 0; i < questions.size(); i++) {
            if(i < studentAnswers.size() && questions.get(i).checkAnswer(studentAnswers.get(i))) {
                correctAnswers++;
            }
        }
        return (correctAnswers * 100.0) / questions.size();
    }
    public int getAttemptsUsed(String username) {
        return studentAttempts.getOrDefault(username, 0);
    }
    public void increaseAttempts(String username) {
        studentAttempts.put(username, getAttemptsUsed(username) + 1);
    }
    public boolean freeAttempts(String username) {
        return getAttemptsUsed(username) < attempts;
    }
    public Map<String, Integer> getStudentAttempts() {
        return studentAttempts;
    }
    public Double getScore(String username) {
        return studentScores.get(username);
    }
    public Map<String, Double > getStudentScores() {
        return studentScores;
    }
    public QuizResult finish(String username,List<Integer> answers){
        double score = calculateScore(answers);
        boolean passed= isPassed(score);
        increaseAttempts(username);
        getStudentScores().put(username, score);
        QuizResult quizResult = new QuizResult(username,getId(), getAttemptsUsed(username), score, passed);

        return quizResult;

    }







}
