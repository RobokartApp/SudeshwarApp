package com.ark.robokart_robotics.Activities.Quiz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Question;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {

    private QuizRepository quizRepository;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        quizRepository = new QuizRepository(application);
    }

    public MutableLiveData<List<Question>> getQuizList() {
        return quizRepository.getQuestionList();
    }
}
