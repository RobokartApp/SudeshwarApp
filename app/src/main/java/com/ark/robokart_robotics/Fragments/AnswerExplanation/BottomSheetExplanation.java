package com.ark.robokart_robotics.Fragments.AnswerExplanation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ark.robokart_robotics.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import carbon.widget.Button;

public class BottomSheetExplanation extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private Button dismiss;

    private TextView question, explaination_txt;
    RadioButton correct_ans;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_explanation_bottomsheet, null);

        dismiss = view.findViewById(R.id.dismiss);
        Log.d("this is bottom sheet","ok");

        question = view.findViewById(R.id.question);
        correct_ans=view.findViewById(R.id.radio_btn_correct);

        explaination_txt = view.findViewById(R.id.explaination_txt);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("explanation", Context.MODE_PRIVATE);
        String qtn = sharedPreferences.getString("question","");
        String exp = sharedPreferences.getString("explaination","");
        String ans = sharedPreferences.getString("answer","");
        int ans_int=Integer.parseInt(ans);
        String ans_correct = sharedPreferences.getString("answer"+ans_int,"1");
        question.setText(qtn);
        explaination_txt.setText(exp);
        correct_ans.setText(ans_correct);
        Log.i("answer_correct is",ans_correct);
        Log.i("answer int",""+ans_int);
        Log.i("answer is",ans);

        dialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setHideable(true);
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(500);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
