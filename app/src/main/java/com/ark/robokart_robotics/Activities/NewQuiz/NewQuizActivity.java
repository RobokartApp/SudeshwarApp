package com.ark.robokart_robotics.Activities.NewQuiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ark.robokart_robotics.Activities.Quiz.QuizViewModel;
import com.ark.robokart_robotics.Model.NewQuizModel;
import com.ark.robokart_robotics.Model.Question;
import com.ark.robokart_robotics.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import carbon.widget.Button;

public class NewQuizActivity extends AppCompatActivity {

    private static final int START_TIME_IN_MILLIS = 600000;

    private MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    private ArrayList<Question> questionArrayList = new ArrayList<>();

    private Button btnNextQustn, btnPreviousQustn;

    private CountDownTimer mCountDownTimer;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private boolean mTimerRunning;

    private ProgressBar countdownProgress;

    private QuizViewModel quizViewModel;

    private RadioGroup rbGroup;
    private RadioButton rb_1, rb_2, rb_3, rb_4;

    private carbon.widget.TextView question_txt;

    private int questionCounter;
    private int questionCountTotal;
    private NewQuizModel currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);
    }

    public void init(){
        rbGroup = findViewById(R.id.rbgroup);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);

        question_txt = findViewById(R.id.question_txt);
    }

    public void listeners(){

        startTimer();
        setProgressBarValues();

    }

    private void startTimer() {


        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                countdownProgress.setProgress((int) (millisUntilFinished / 1000));

                updateCountDownText();

                long elapsed_time;

                elapsed_time = 10800000-millisUntilFinished;



            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    private void setProgressBarValues() {

        countdownProgress.setMax((int) mTimeLeftInMillis / 1000);
        countdownProgress.setProgress((int) mTimeLeftInMillis / 1000);
    }


    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

    }

    //    To go to next question
    private void showNextQuestion(List<NewQuizModel> questionList) {
        rbGroup.clearCheck();

        questionCountTotal = questionList.size();


        if (questionCounter < questionCountTotal) {

            int new_counter = questionCounter;
            new_counter++;
//            tvQuestionCount.setText("Question "+ new_counter + " /");

            currentQuestion = questionList.get(questionCounter);

//            tvTotalQuestion.setText(String.valueOf(questionCountTotal));

            question_txt.setText(currentQuestion.getQuestion_name());
            rb_1.setText(currentQuestion.getAnswer1());
            rb_2.setText(currentQuestion.getAnswer2());
            rb_3.setText(currentQuestion.getAnswer3());
            rb_4.setText(currentQuestion.getAnswer4());
            questionCounter++;

            if(questionCounter == 10){
                btnNextQustn.setText("FINISH");
            }
        }

        else {

        }
    }


    //    To go back to previous question
    private void showPreviousQuestion(List<NewQuizModel> questionList) {
        rbGroup.clearFocus();

        int new_counter = questionCounter;
        if(new_counter != 1){
            new_counter--;
            questionCounter--;
        }

//        tvQuestionCount.setText(String.format(Locale.ENGLISH,"Question %d /", new_counter));

        currentQuestion = questionList.get(questionCounter - 1);

        question_txt.setText(currentQuestion.getQuestion_name());
        rb_1.setText(currentQuestion.getAnswer1());
        rb_2.setText(currentQuestion.getAnswer2());
        rb_3.setText(currentQuestion.getAnswer3());
        rb_4.setText(currentQuestion.getAnswer4());

    }
}