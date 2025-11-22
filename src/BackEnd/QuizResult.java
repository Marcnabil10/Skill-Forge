package BackEnd;

import org.json.JSONObject;

import java.util.List;

public class QuizResult {
    private String username;
    private String quizId;
    private double score;
    private int attempts;
    private boolean passed;
    public QuizResult(String username, String quizId, int attempts, double score, boolean passed){
        this.username=username;
        this.quizId =quizId;
        this.attempts=attempts;
        this.score=score;
        this.passed=passed;
    }

    public String getUsername() {
        return username;
    }

    public String getQuizId() {
        return quizId;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("quizId", quizId);
        json.put("score", score);
        json.put("attemptsUsed", attempts);
        json.put("passed", passed);
        return json;
    }
    public QuizResult(JSONObject json) {
        this.username = json.getString("username");
        this.quizId = json.getString("quizId");
        this.score = json.getDouble("score");
        this.attempts = json.getInt("attemptsUsed");
        this.passed = json.getBoolean("passed");
    }



}
