package com.ark.robokart_robotics.Activities.Quiz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.Question;

import java.util.List;

public class PlayContestViewModel extends AndroidViewModel {

    private final PlayContestRepository quizRepository;

    public PlayContestViewModel(@NonNull Application application) {
        super(application);
        quizRepository = new PlayContestRepository(application);
    }

    public MutableLiveData<List<Question>> getQuizList() {
        return quizRepository.getQuestionList();
    }

  /*  public MutableLiveData<String> addQuizList(String course_id, String quiz_id, String customer_id,
                                               String login_username, String login_parents_number, String login_mobile,
                                               String total_number_of_chapter, String quiz_counter, String total_right,
                                               String total_wrong, String total, String percent) {
        return quizRepository.addQuizResult(course_id,quiz_id,customer_id,login_username,login_parents_number,login_mobile,total_number_of_chapter,quiz_counter,total_right,total_wrong,total,percent);
    }*/
}

