package com.ark.robokart_robotics.Activities.Quiz;

import java.util.ArrayList;

public class QuizLog {
    private String quiz_id,quiz_date,result,count;
    int isGiven;

    public QuizLog(String quiz_id, String quiz_date,String result,String count,int isGiven) {
        this.quiz_id=quiz_id;
        this.quiz_date=quiz_date;
        this.result=result;
        this.isGiven=isGiven;
        this.count=count;
    }

    public String getQuiz_date() {
        return quiz_date;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getResult() {
        return result;
    }

    public int getIsGiven() {
        return isGiven;
    }

    public String getCount() {
        return count;
    }
}
