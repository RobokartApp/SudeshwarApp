package com.ark.robokart_robotics.Activities.Quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Fragments.Dashboard.New_Dashboard;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

public class DailyQuizActivity extends AppCompatActivity {
ImageView back_btn;
Button playQuiz;
TextView instrc;
RecyclerView recyclerView;
QuizLogAdapter quizLogAdapter;
ArrayList<QuizLog> quizLogList;
    private RequestQueue requestQueue;
    String user_id;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_quiz);

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id","848");

        init();
        listeners();

        getQuizLog();

    }

    private void getQuizLog() {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.quiz_log_api, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray quiz = jsonObject.getJSONArray("daily_quiz");
                int status = jsonObject.getInt("success_code");
                String error_msg = jsonObject.getString("error_msg");
                Log.i("DailyQuizAct",response);
                quizLogList=new ArrayList<>();

                if (status == 1) {
                    try{
                        for(int i = 0; i< quiz.length();i++){
                            JSONObject json = quiz.getJSONObject(i);
                            String quiz_id=json.getString("quiz_id");
                            String quiz_date=json.getString("quiz_date");
                            String result=json.getString("result");
                            String total=json.getString("total");
                            int isGiven=json.getInt("isGiven");

                            quizLogList.add(new QuizLog(quiz_id,quiz_date,result,total,isGiven));

                            //break;
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else if (status == 0) {
                    //Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "No internet connection. Try again!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DailyQuiz Volley", ": "+e.getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Volley error: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("customer_id",user_id);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                quizLogAdapter=new QuizLogAdapter(quizLogList);
                recyclerView.setAdapter(quizLogAdapter);
                quizLogAdapter.setOnItemClickListener(new QuizLogAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String abc=quizLogList.get(position).getQuiz_date();
                        //Toast.makeText(DailyQuizActivity.this, "item:"+abc, Toast.LENGTH_SHORT).show();
                        if (position!=0){
                            Toast.makeText(DailyQuizActivity.this, "This is past quiz!", Toast.LENGTH_SHORT).show();
                        }else if(quizLogList.get(position).getIsGiven()==1){
                            makeDialog();
                        }
                        else{
                            Intent intent=new Intent(getApplicationContext(),PlayQuizActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
        });
    }

    private void makeDialog() {
        new AlertDialog.Builder(this)
                .setMessage("You have already attended!")
                .setPositiveButton("ReAttempt", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent=new Intent(getApplicationContext(),PlayQuizActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                    }
                })
                .show();
    }

    private void init() {

        requestQueue = Volley.newRequestQueue(this);
        recyclerView=findViewById(R.id.rvQuizLog);
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange);

        instrc=findViewById(R.id.instructions);
        back_btn=findViewById(R.id.back_btn);
        playQuiz=findViewById(R.id.play_qz_btn);
        String instrString="";
        for(String s: New_Dashboard.instructions){
            instrString+="- "+s+"\n";
        }
        instrc.setText(instrString);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instrc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }
    private void listeners() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        playQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizLogList.get(0).getIsGiven()==1)
                    makeDialog();
                else {
                    Intent intent = new Intent(getApplicationContext(), PlayQuizActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);
                startActivity(new Intent(DailyQuizActivity.this,DailyQuizActivity.class));
                finish();
            }
        });
    }
}