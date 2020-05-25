package com.ark.robokart_robotics.Activities.Quiz;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizRepository {

    private static final String TAG = "QuizRepository";

    private Application application;

    private RequestQueue requestQueue;

    private MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    private ArrayList<Question> questionArrayList = new ArrayList<>();

    public QuizRepository(Application application){
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
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

                        questionList.setValue(questionArrayList);
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

        return questionList;
    }
}
