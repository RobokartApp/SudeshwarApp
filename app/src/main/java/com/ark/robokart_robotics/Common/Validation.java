package com.ark.robokart_robotics.Common;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.ark.robokart_robotics.R;
import com.google.android.material.textfield.TextInputEditText;

import static java.security.AccessController.getContext;

public class Validation {

    public void validatePhone(EditText editText, TextView layout) {

        String number = editText.getText().toString();

        if(number.equals("")){
            //layout.setError("Please Enter Email ID");
            layout.setVisibility(View.VISIBLE);

            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_outline, 0);
        }
        else{

            if(number.length() == 10){
                //layout.setError(null);
                layout.setVisibility(View.GONE);

                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green, 0);
            }
            else{
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_outline, 0);
                //layout.setError("Please Enter Valid Email ID");
                layout.setVisibility(View.VISIBLE);
            }

        }

    }

}
