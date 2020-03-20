package com.ark.robokart_robotics.Fragments.AnswerExplanation;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.ark.robokart_robotics.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetExplanation extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_explanation_bottomsheet, null);


        dialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
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