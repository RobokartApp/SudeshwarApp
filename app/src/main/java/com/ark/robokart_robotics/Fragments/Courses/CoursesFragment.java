package com.ark.robokart_robotics.Fragments.Courses;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.MyCertificatesAdapter;
import com.ark.robokart_robotics.Adapters.MyCoursesAdapter;
import com.ark.robokart_robotics.Common.ApiConstants;
import com.ark.robokart_robotics.Fragments.Dashboard.DashboardFragment;
import com.ark.robokart_robotics.Model.MyCertificateModel;
import com.ark.robokart_robotics.Model.MyCoursesModel;
import com.ark.robokart_robotics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.widget.Button;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
//import static com.facebook.FacebookSdk.getApplicationContext;

public class CoursesFragment extends Fragment {

    private RecyclerView myCoursesRecyclerview,CompletedCoursesRecyclerview,certificate_rv;

    private CoursesViewModel coursesViewModel;
    private CompletedCoursesViewModel CompletedCoursesViewModel;

    private MyCoursesAdapter myCoursesAdapter, CompletedCoursesAdapter;

    private LottieAnimationView animationView;

    String customer_id="";
    public static ProgressBar progressBar;
    public static TextView no_courses;
    public static Button view_courses;
    String TAG="courseFrag";
    private RequestQueue requestQueue;

    private final ArrayList<MyCertificateModel> certificateList = new ArrayList<>();


    public CoursesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Menu menu= HomeActivity.bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);

        return inflater.inflate(R.layout.fragment_courses,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        requestQueue = Volley.newRequestQueue(getContext());
        coursesViewModel =  new ViewModelProvider(this).get(CoursesViewModel.class);
        CompletedCoursesViewModel =  new ViewModelProvider(this).get(CompletedCoursesViewModel.class);

        init(view);

        listeners();
        getCertificates();
    }

    private void getCertificates() {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.HOST + ApiConstants.mycertificates, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("certificate respo",response);
                JSONArray courses = jsonObject.getJSONArray("cert");
                int status = jsonObject.getInt("success_code");

                if (status == 1) {
                    try{
                        for(int i = 0; i< courses.length();i++){
                            JSONObject json = courses.getJSONObject(i);
                            MyCertificateModel course = new MyCertificateModel(
                                    json.getString("course_name"),
                                    json.getString("date"),
                                    json.getString("cert_number"),
                                    json.getString("result_id")

                            );
                            certificateList.add(course);
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
                parameters.put("customer_id",customer_id);
                return parameters;
            }
        };
        requestQueue.add(request);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                Log.i("mycourses size",""+certificateList.size());

               MyCertificatesAdapter adapter=new MyCertificatesAdapter(certificateList,getContext());
               if (certificateList.size()>0)
                certificate_rv.setAdapter(adapter);

            }
        });
    }

    public void init(View v){
        progressBar=v.findViewById(R.id.progressBar);

        no_courses=v.findViewById(R.id.no_courses_tv);
        view_courses=v.findViewById(R.id.view_courses_cbtn);
        //animationView = v.findViewById(R.id.animationView);

        certificate_rv=v.findViewById(R.id.CertificateRecyclerView);
        myCoursesRecyclerview = v.findViewById(R.id.myCoursesRecyclerview);
        CompletedCoursesRecyclerview = v.findViewById(R.id.CompletedCoursesRecyclerview);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userdetails",MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id","0");

        coursesViewModel.getMyCoursesList(customer_id).observe(getActivity(), new Observer<List<MyCoursesModel>>() {
            @Override
            public void onChanged(List<MyCoursesModel> courseListModels) {
                myCoursesAdapter = new MyCoursesAdapter(getContext(),courseListModels);
                myCoursesRecyclerview.setAdapter(myCoursesAdapter);
                myCoursesAdapter.notifyDataSetChanged();

            }
        });

        CompletedCoursesViewModel.getMyCoursesListComplete(customer_id).observe(getActivity(), new Observer<List<MyCoursesModel>>() {
            @Override
            public void onChanged(List<MyCoursesModel> courseListModels) {
                CompletedCoursesAdapter = new MyCoursesAdapter(getContext(),courseListModels);
                CompletedCoursesRecyclerview.setAdapter(CompletedCoursesAdapter);
                CompletedCoursesAdapter.notifyDataSetChanged();

            }
        });
    }

    public void listeners(){
view_courses.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DashboardFragment uploadDoc= new DashboardFragment();
        (getActivity()).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim)
                .replace(R.id.mainFrameLayout, uploadDoc, "mycourses")
                .addToBackStack(null)
                .commit();
    }
});
    }

}
