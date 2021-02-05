package com.ark.robokart_robotics.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.Activities.AtlChooseStandard.AtlChooseStandard;
import com.ark.robokart_robotics.Adapters.MyAdapter;
import com.ark.robokart_robotics.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;

public class AtlCourseDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    private TextView tvName,tvGood,docTitle;
    ImageView back_btn,left,right;
    String title,std;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atl_course_details);
        int indx=Integer.parseInt(AtlChooseLevel.indx);
        title=AtlChooseLevel.title.get(indx-1);
        std= AtlChooseStandard.std;

        init();

        listeners();
docTitle.setText(std+"."+(indx)+" "+title);
String circuit=""+AtlChooseLevel.circuit.get(indx-1);
        String code=""+AtlChooseLevel.code.get(indx-1);
        //Toast.makeText(this, ": "+circuit+", "+code, Toast.LENGTH_SHORT).show();
        tabLayout.addTab(tabLayout.newTab().setText("Problem Statement"));
        tabLayout.addTab(tabLayout.newTab().setText("Components"));
        tabLayout.addTab(tabLayout.newTab().setText("Procedure"));
        if(!circuit.equals("NA") && !circuit.isEmpty()){
            tabLayout.addTab(tabLayout.newTab().setText("Circuit"));
            //Toast.makeText(this, "ok Circuit", Toast.LENGTH_SHORT).show();
        }
        tabLayout.addTab(tabLayout.newTab().setText("Output"));
        if(!code.equals("NA") && !code.isEmpty())
            tabLayout.addTab(tabLayout.newTab().setText("Code"));
        tabLayout.addTab(tabLayout.newTab().setText("Quiz"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount(),tabLayout);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)left.setVisibility(View.GONE);
                else left.setVisibility(View.VISIBLE);
                if(tab.getPosition()==(tabLayout.getTabCount()-1))right.setVisibility(View.GONE);
                else right.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void init(){
        left=findViewById(R.id.left_ic);
        right=findViewById(R.id.right_ic);
        docTitle=findViewById(R.id.docTitle);
        tabLayout= findViewById(R.id.tabLayout);
        viewPager= findViewById(R.id.viewPager);
        tvName = findViewById(R.id.tvName);
        back_btn = findViewById(R.id.back_btn);
        tvGood = findViewById(R.id.tvGood);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("userdetails",MODE_PRIVATE);
            String fullname = sharedPreferences.getString("fullname","-");

            String lastName = "";
            String firstName= "";
            if(fullname.split("\\w+").length>1){

                lastName = fullname.substring(fullname.lastIndexOf(" ")+1);
                firstName = fullname.substring(0, fullname.lastIndexOf(' '));
            }
            else{
                firstName = fullname;
            }

            tvName.setText(firstName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        //Set greeting
        String greeting = null;
        if(hour>=6 && hour<12){
            greeting = "Good Morning,";
        } else if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon,";
        } else if(hour >= 17 && hour < 24){
            greeting = "Good Evening,";
        }
        else if(hour >= 0 && hour < 4){
            greeting = "Good Night,";
        }

        tvGood.setText(greeting);
    }
    public void listeners() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem()!=0)
                viewPager.setCurrentItem(getItem(-1), true); //getItem(-1) for previous
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==tabLayout.getTabCount());
                    viewPager.setCurrentItem(getItem(+1), true);
            }
        });
    }
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}