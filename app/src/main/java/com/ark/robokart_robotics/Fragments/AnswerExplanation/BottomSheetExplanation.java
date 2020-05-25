package com.ark.robokart_robotics.Fragments.AnswerExplanation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_explanation_bottomsheet, null);

        dismiss = view.findViewById(R.id.dismiss);

        question = view.findViewById(R.id.question);

        explaination_txt = view.findViewById(R.id.explaination_txt);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("explanation", Context.MODE_PRIVATE);
        String qtn = sharedPreferences.getString("question","");
        String exp = sharedPreferences.getString("explaination","");

        question.setText(qtn);
        explaination_txt.setText(exp);

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
