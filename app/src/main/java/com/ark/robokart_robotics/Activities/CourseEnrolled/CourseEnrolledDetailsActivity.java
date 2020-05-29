package com.ark.robokart_robotics.Activities.CourseEnrolled;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Adapters.ChapterContentAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Model.ChapterContent;
import com.ark.robokart_robotics.Model.ChapterName;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseEnrolledDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CourseEnrolledDetailsAc";

    private RecyclerView chapterContentRecyclerview;

    private CourseEnrolledDetailsViewModel courseEnrolledDetailsViewModel;

    private ChapterContentAdapter chapterContentAdapter;

    private ArrayList<ChapterName> chapterNameArrayList = new ArrayList<>();

    private ArrayList<ChapterContent> chapterContentArrayList = new ArrayList<>();

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_enrolled_details);

        courseEnrolledDetailsViewModel = new ViewModelProvider(this).get(CourseEnrolledDetailsViewModel.class);

        init();

        listeners();

    }

    public void init(){
        chapterContentRecyclerview = findViewById(R.id.chapterContentRecyclerview);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    public void listeners(){

        courseEnrolledDetailsViewModel.getChapterName("15").observe(this, new Observer<List<ChapterName>>() {
            @Override
            public void onChanged(List<ChapterName> chapterNames) {
                chapterContentAdapter = new ChapterContentAdapter(chapterNames);
                chapterContentRecyclerview.setAdapter(chapterContentAdapter);
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        chapterContentAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        chapterContentAdapter.onRestoreInstanceState(savedInstanceState);
    }
}
