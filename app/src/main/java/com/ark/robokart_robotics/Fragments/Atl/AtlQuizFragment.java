package com.ark.robokart_robotics.Fragments.Atl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.NewQuiz.NewQuizActivity;
import com.ark.robokart_robotics.Activities.Quiz.PlayQuizActivity;
import com.ark.robokart_robotics.R;

public class AtlQuizFragment extends Fragment {

    public AtlQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atl_quiz, container, false);
    }
    Button playQuiz;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playQuiz=view.findViewById(R.id.play_qz_btn);

        playQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewQuizActivity.class));
            }
        });
    }
}