package com.ark.robokart_robotics.Activities.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Adapters.CheckAnswerAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.CorrectAnswersModel;
import com.ark.robokart_robotics.Model.CourseListModel;
import com.ark.robokart_robotics.Model.Question;
import com.ark.robokart_robotics.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import carbon.widget.Button;

public class QuizActivity extends AppCompatActivity {

    private static final int START_TIME_IN_MILLIS = 600000;

    private static final String TAG = "QuizActivity";

    private RequestQueue requestQueue;

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

    public static ArrayList<CorrectAnswersModel> correctAnswersList = new ArrayList<>();

    public static ArrayList<String> answersGivenList = new ArrayList<>();

    private Button btnNextQustn, btnPreviousQustn;

    private int score;
    private boolean answered;

    private LinearLayout bottom_sheet;
    private BottomSheetBehavior sheetBehavior;



    private ImageView close_btn, ivClose;

    private carbon.widget.TextView tvTotal,tvQuestionAnswered, tvCorrectAnswers, tvPassingScore, tvTimeTaken;

    private carbon.widget.TextView tvTotalQuestion, tvQuestionCount;

    private RecyclerView answerRecyclerview;

    private CheckAnswerAdapter checkAnswerAdapter;

    int old_seconds = 0;

    int seconds = 0;

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

        requestQueue = Volley.newRequestQueue(getApplicationContext());

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

        answerRecyclerview = findViewById(R.id.answersRecyclerview);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.canScrollVertically();
        answerRecyclerview.setLayoutManager(layoutManager);

        checkAnswerAdapter = new CheckAnswerAdapter(getApplicationContext(),questionArrayList);





        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        startTimer();
        setProgressBarValues();

      quizViewModel.getQuizList().observe(this, new Observer<List<Question>>() {
          @Override
          public void onChanged(List<Question> questionList) {
              showNextQuestion(questionList);
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
        StringRequest request = new StringRequest(Request.Method.GET, ApiConstants.HOST + ApiConstants.fetchquiz_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray quiz = jsonObject.getJSONArray("quiz");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");

                if (status == 1) {
                    try{
                        for(int i = 0; i< quiz.length();i++){
                            JSONObject json = quiz.getJSONObject(i);
                            Question question = new Question(
                                    json.getInt("q_no"),
                                    json.getString("question_name"),
                                    json.getString("answer1"),
                                    json.getString("answer2"),
                                    json.getString("answer3"),
                                    json.getString("answer4"),
                                    json.getInt("answer"),
                                    json.getString("answer_explaination")
                            );
                            questionArrayList.add(question);
                        }
                    }
                    catch (Exception e){
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "fetchLocationListing: "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                return parameters;
            }
        };
        requestQueue.add(request);
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

                seconds = (int) (elapsed_time / 1000) % 60;


                old_seconds = old_seconds + seconds;

                tvTimeTaken.setText(seconds + "s");

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
            mCountDownTimer.cancel();
            countdownProgress.setProgress(0);

            tvTotal.setText(String.valueOf(questionCountTotal));

            tvCorrectAnswers.setText(String.valueOf(score));

            double passing = (score / Double.parseDouble(String.valueOf(questionCountTotal))) * 100;

            tvPassingScore.setText(String.format("%.2f", passing)+"%");

            tvQuestionAnswered.setText(String.valueOf(answersGivenList.size()));

            answerRecyclerview.setAdapter(checkAnswerAdapter);
            checkAnswerAdapter.notifyDataSetChanged();

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

            question_txt.setText(currentQuestion.getQuestion_name());
            rb_1.setText(currentQuestion.getAnswer1());
            rb_2.setText(currentQuestion.getAnswer2());
            rb_3.setText(currentQuestion.getAnswer3());
            rb_4.setText(currentQuestion.getAnswer4());

    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        try {
            int id = rbSelected.getId();
            if(id == 0){
                id = 0;
                answersGivenList.add(String.valueOf(id));
            }
            else{
                answersGivenList.add(String.valueOf(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (answerNr == currentQuestion.getAnswer()) {
                correctAnswersList.add(new CorrectAnswersModel(answerNr));
            score++;
            }
            else{
                answerNr = 0;
                correctAnswersList.add(new CorrectAnswersModel(answerNr));
            }


    }
}
