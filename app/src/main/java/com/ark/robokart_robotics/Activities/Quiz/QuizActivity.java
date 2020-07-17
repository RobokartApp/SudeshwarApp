package com.ark.robokart_robotics.Activities.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.VideoPlaying.VideoPlayingActivity;
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

    private TextView mTextViewCountDown, tv_pass_fail;

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
    private int right_answer;
    private int wrong_answer;

    private LinearLayout bottom_sheet;
    private BottomSheetBehavior sheetBehavior;



    private ImageView close_btn, ivClose;

    private carbon.widget.TextView tvTotal,tvQuestionAnswered, tvCorrectAnswers, tvPassingScore, tvTimeTaken;

    private carbon.widget.TextView tvTotalQuestion, tvQuestionCount;

    private Button go_home_btn;

    private ImageView re_attempt_btn;


    private RecyclerView answerRecyclerview;

    private CheckAnswerAdapter checkAnswerAdapter;

    int old_seconds = 0;

    int seconds = 0;

    String quiz_id;
    
    String username = "";

    String student_number = "";

    String parents_number = "";

    String customer_id = "";

    String course_id = "";

    String total_number_of_chapter = "";

    String quiz_counter = "";

    String right = "";

    String wrong = "";

    String count = "";

    String percent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            customer_id = sharedPreferences.getString("customer_id","");
            username = sharedPreferences.getString("username","");
            student_number = sharedPreferences.getString("stud_number","");
            parents_number = sharedPreferences.getString("parent_number","");
            SharedPreferences quiz = getSharedPreferences("quiz_id",MODE_PRIVATE);
            quiz_id = quiz.getString("quiz_id","");
            Log.d(TAG, "After quiz_id: "+quiz_id);

            Bundle bundle = getIntent().getExtras();
            course_id = bundle.getString("course_id");
            total_number_of_chapter = bundle.getString("total_number_chapter");
            quiz_counter = bundle.getString("chpt_id");

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        tv_pass_fail = findViewById(R.id.tv_pass_fail);

        answerRecyclerview = findViewById(R.id.answersRecyclerview);

        go_home_btn = findViewById(R.id.go_home_btn);

        re_attempt_btn = findViewById(R.id.re_attempt_btn);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.canScrollVertically();
        answerRecyclerview.setLayoutManager(layoutManager);


        VideoPlayingActivity videoPlayingActivity = new VideoPlayingActivity();


        checkAnswerAdapter = new CheckAnswerAdapter(getApplicationContext(),questionArrayList);





        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        startTimer();
        setProgressBarValues();

      quizViewModel.getQuizList(quiz_id).observe(this, new Observer<List<Question>>() {
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

        re_attempt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getQuestionList();
//                countdownProgress.setProgress(100);
//                mTimeLeftInMillis = START_TIME_IN_MILLIS;
//                startTimer();

               startActivity(new Intent(getApplicationContext(),QuizActivity.class));
               finish();

            }
        });

        go_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                videoPlayingActivity.finish();
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
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


//    To get all questions of quiz
    public MutableLiveData<List<Question>> getQuestionList() {

        questionArrayList.clear();

        correctAnswersList.clear();

        answersGivenList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.fetchquiz_api, response -> {
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
                parameters.put("quiz_id",quiz_id);
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

//    To go to next question
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

            percent = tvPassingScore.getText().toString().trim();

            count = tvTotal.getText().toString().trim();

            right = tvCorrectAnswers.getText().toString().trim();

            wrong = String.valueOf(wrong_answer);

            tvQuestionAnswered.setText(String.valueOf(answersGivenList.size()));

            answerRecyclerview.setAdapter(checkAnswerAdapter);
            checkAnswerAdapter.notifyDataSetChanged();

            if(passing < 60) {
                tv_pass_fail.setText("Oops!!\n Try Again. You were nearly there.");
            }
            else {
                tv_pass_fail.setText("Hooray!! New Chapter Unlocked!");
            }

            submitQuizResult(course_id,quiz_id,customer_id,username,parents_number,student_number,total_number_of_chapter,quiz_counter,right,wrong,count,percent);

            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        }
    }


//    To go back to previous question
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


    //On Last Question
    public void submitQuizResult(String course_id, String quiz_id, String customer_id,
                                 String login_username, String login_parents_number, String login_mobile,
                                 String total_number_of_chapter, String quiz_counter, String total_right,
                                 String total_wrong, String total, String percent){

        quizViewModel.addQuizList(course_id,quiz_id,customer_id,login_username,login_parents_number,login_mobile,total_number_of_chapter,quiz_counter,total_right,total_wrong,total,percent).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("learn percent updated in orders table.")) {

                }
                else {

                }
            }
        });

    }

//    To Check answer
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
            right_answer++;
            }
            else{
                answerNr = 0;
                wrong_answer++;
                correctAnswersList.add(new CorrectAnswersModel(answerNr));
            }

    }
}
