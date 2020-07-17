package com.ark.robokart_robotics.Fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ark.robokart_robotics.Activities.AtlChooseStandard.AtlChooseStandard;
import com.ark.robokart_robotics.Activities.ChooseCategory.ChooseCategory;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Activities.NewQuiz.NewQuizActivity;
import com.ark.robokart_robotics.R;

public class New_Dashboard extends Fragment {

    public ImageView play_quiz_challenge;

    public CardView cardMaker, cardAtl;

    public New_Dashboard(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_new,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardMaker = view.findViewById(R.id.cardMaker);
        cardAtl = view.findViewById(R.id.cardAtl);
        play_quiz_challenge = view.findViewById(R.id.play_quiz_challenge);


        cardMaker.setOnClickListener(new View.OnClickListener() {
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

        cardAtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AtlChooseStandard.class);
                startActivity(intent);
            }
        });

        play_quiz_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseCategory.class);
                startActivity(intent);
            }
        });
    }
}
