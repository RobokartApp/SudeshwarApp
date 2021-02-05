package com.ark.robokart_robotics.Fragments.Atl;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.R;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

public class ProbStatFrag extends Fragment {

    public ProbStatFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
TextView fragText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prob_stat, container, false);
    }
String indx,prob_stat;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragText=view.findViewById(R.id.fragText);
        //fragText.setText("- This is first statement\n- This is second statement");
        int indx=Integer.parseInt(AtlChooseLevel.indx);
        prob_stat=AtlChooseLevel.prob_stat.get(indx-1);
        String[] strArr=prob_stat.split("#");
        String op="";int i=1;
        for(String s:strArr) {
            if(!s.isEmpty()) {
                op += "- "+ s + "\n";
                i++;
            }
        }
        fragText.setText(op);


    }
}