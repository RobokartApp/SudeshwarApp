package com.ark.robokart_robotics.Model;

public class Question {

    public int q_no;
    public String question_name;
    public String answer1;
    public String answer2;
    public String answer3;
    public String answer4;
    public int answer;
    public String answer_explaination;

    public Question(int q_no, String question_name, String answer1, String answer2, String answer3, String answer4, int answer, String answer_explaination) {
        this.q_no = q_no;
        this.question_name = question_name;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer = answer;
        this.answer_explaination = answer_explaination;
    }

    public int getQ_no() {
        return q_no;
    }

    public void setQ_no(int q_no) {
        this.q_no = q_no;
    }

    public String getQuestion_name() {
        return question_name;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAnswer_explaination() {
        return answer_explaination;
    }

    public void setAnswer_explaination(String answer_explaination) {
        this.answer_explaination = answer_explaination;
    }
}


