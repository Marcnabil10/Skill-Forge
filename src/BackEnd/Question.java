package BackEnd;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.ArrayList;



public class Question {
    private String questionText;
    private List<String> options= new ArrayList<>();
    private int correctIndex;

    public Question(String questionText ,String option0 ,String option1 ,String option2, String option3, int correctIndex){
        this.questionText=questionText;
        this.correctIndex=correctIndex;
        options.add(0,option0);
        options.add(1,option1);
        options.add(2,option2);
        options.add(3,option3);
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }
    public Boolean checkAnswer(int answerIndex){
        return answerIndex == correctIndex;
    }
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("questionText", this.questionText);
        json.put("options", new JSONArray(this.options));
        json.put("correctAnswerIndex", this.correctIndex);
        return json;
    }
    public Question(JSONObject json) {
        this.questionText = json.getString("questionText");
        this.options = new ArrayList<>();
        JSONArray arr = json.getJSONArray("options");
        for (int i = 0; i < arr.length(); i++) {
            this.options.add(arr.getString(i));
        }
        this.correctIndex = json.getInt("correctAnswerIndex");
    }




}
