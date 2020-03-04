package com.ark.robokart_robotics.Activities.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ark.robokart_robotics.Model.Question;
import com.ark.robokart_robotics.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import carbon.widget.Button;

public class QuizActivity extends AppCompatActivity {

    private static final int START_TIME_IN_MILLIS = 600000;


    private TextView mTextViewCountDown;

    private CountDownTimer mCountDownTimer;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private boolean mTimerRunning;

    private ProgressBar countdownProgress;

    private QuizViewModel quizViewModel;

    private RadioGroup rbGroup;
    private RadioButton rb_1, rb_2, rb_3, rb_4;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private carbon.widget.TextView question_txt;

    private MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    private ArrayList<Question> questionArrayList = new ArrayList<>();

    private Button btnNextQustn, btnPreviousQustn;

    private int score;
    private boolean answered;

    private LinearLayout bottom_sheet;
    private BottomSheetBehavior sheetBehavior;

    private ImageView close_btn, ivClose;

    private carbon.widget.TextView tvTotal,tvQuestionAnswered, tvCorrectAnswers, tvPassingScore, tvTimeTaken;

    private carbon.widget.TextView tvTotalQuestion, tvQuestionCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();


        getQuestionList();
    }

    public void init() {
        mTextViewCountDown = findViewById(R.id.timer_text);

        countdownProgress = findViewById(R.id.countdownProgress);


        rbGroup = findViewById(R.id.rbgroup);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);

        ivClose = findViewById(R.id.ivClose);

        tvQuestionCount = findViewById(R.id.tvQuestionCount);

        tvTotalQuestion = findViewById(R.id.tvTotalQuestion);

        question_txt = findViewById(R.id.question_txt);

        btnNextQustn = findViewById(R.id.btnNextQustn);

        btnPreviousQustn = findViewById(R.id.btnPreviousQustn);

        bottom_sheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        tvTotal = findViewById(R.id.tvTotal);

        tvQuestionAnswered = findViewById(R.id.tvQuestionAnswered);

        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);

        tvPassingScore = findViewById(R.id.tvPassingScore);

        tvTimeTaken = findViewById(R.id.tvTimeTaken);

        close_btn = findViewById(R.id.close_btn);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        startTimer();
        setProgressBarValues();

        quizViewModel.getQuizList().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                showNextQuestion(questions);
            }
        });

        btnNextQustn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                showNextQuestion(questionArrayList);
            }
        });

        btnPreviousQustn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    checkAnswer();
                    showPreviousQuestion(questionArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (questionCounter == questionCountTotal) {
            super.onBackPressed();
        }
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }

    public MutableLiveData<List<Question>> getQuestionList() {
        questionArrayList.add(new Question("Q1", "a", "b", "c", "d", 1));
        questionArrayList.add(new Question("Q2", "a", "b", "c", "d", 3));
        questionArrayList.add(new Question("Q3", "a", "b", "c", "d", 2));
        questionArrayList.add(new Question("Q4", "a", "b", "c", "d", 1));
        questionArrayList.add(new Question("Q5", "a", "b", "c", "d", 2));
        questionArrayList.add(new Question("Q6", "a", "b", "c", "d", 4));
        questionArrayList.add(new Question("Q7", "a", "b", "c", "d", 3));
        questionList.setValue(questionArrayList);
        return questionList;
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

                int seconds = (int) (elapsed_time / 1000) % 60;

                        tvTimeTaken.setText(String.valueOf(seconds)+"s");

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




        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void showNextQuestion(List<Question> questionList) {
        rbGroup.clearCheck();

        questionCountTotal = questionList.size();





        if (questionCounter < questionCountTotal) {

            int new_counter = questionCounter;
            new_counter++;
            tvQuestionCount.setText("Question "+ new_counter + " /");

            currentQuestion = questionList.get(questionCounter);

            tvTotalQuestion.setText(String.valueOf(questionCountTotal));




            question_txt.setText(currentQuestion.getQuestion());
            rb_1.setText(currentQuestion.getOption1());
            rb_2.setText(currentQuestion.getOption2());
            rb_3.setText(currentQuestion.getOption3());
            rb_4.setText(currentQuestion.getOption4());
            questionCounter++;

            if(questionCounter == 6){
                btnNextQustn.setText("FINISH");
            }

        }

        else {
            mCountDownTimer.cancel();
            countdownProgress.setProgress(0);

            tvTotal.setText(String.valueOf(questionCountTotal));

            tvCorrectAnswers.setText(String.valueOf(score));

            double passing = (score / Double.parseDouble(String.valueOf(questionCountTotal))) * 100;

            tvPassingScore.setText(String.format("%.2f", passing)+"%");



            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }


    }


    private void showPreviousQuestion(List<Question> questionList) {
            rbGroup.clearFocus();

            int new_counter = questionCounter;
            if(new_counter != 1){
                new_counter--;
                questionCounter--;
            }

            tvQuestionCount.setText(String.format(Locale.ENGLISH,"Question %d /", new_counter));

            currentQuestion = questionList.get(questionCounter - 1);

            question_txt.setText(currentQuestion.getQuestion());
            rb_1.setText(currentQuestion.getOption1());
            rb_2.setText(currentQuestion.getOption2());
            rb_3.setText(currentQuestion.getOption3());
            rb_4.setText(currentQuestion.getOption4());

    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            Toast.makeText(getApplicationContext(), String.valueOf(score), Toast.LENGTH_SHORT).show();
        }
    }
}
