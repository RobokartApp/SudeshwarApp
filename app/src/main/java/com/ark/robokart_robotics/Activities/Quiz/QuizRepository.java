package com.ark.robokart_robotics.Activities.Quiz;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizRepository {

    private Application application;

    private MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    private ArrayList<Question> questionArrayList = new ArrayList<>();

    public QuizRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<List<Question>> getQuestionList() {
        questionArrayList.add(new Question(1,"Q1", "a", "b", "c", "d", 1));
        questionArrayList.add(new Question(2,"Q2", "a", "b", "c", "d", 3));
        questionArrayList.add(new Question(3,"Q3", "a", "b", "c", "d", 2));
        questionArrayList.add(new Question(4,"Q4", "a", "b", "c", "d", 1));
        questionArrayList.add(new Question(5,"Q5", "a", "b", "c", "d", 2));
        questionArrayList.add(new Question(6,"Q6", "a", "b", "c", "d", 4));
        questionArrayList.add(new Question(7,"Q7", "a", "b", "c", "d", 3));
        questionList.setValue(questionArrayList);
        return questionList;
    }
}
