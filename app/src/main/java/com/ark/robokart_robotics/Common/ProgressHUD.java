package com.ark.robokart_robotics.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.robokart_robotics.R;

import java.util.Objects;

public class ProgressHUD extends Dialog {
	/*public ProgressHUD(Context context) {
		super(context);
	}*/

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus){

        ImageView imageView = findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

	/*public void setMessage(CharSequence message) {
		if(message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);
			TextView txt = findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}*/

    public ProgressHUD show(Activity context, final CharSequence message) {
        //final ProgressHUD dialog = new ProgressHUD(context,R.style.ProgressHUD);


        context.runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                setTitle("");
                setContentView(R.layout.progress_hud);
                setCancelable(false);
                if(message == null || message.length() == 0) {
                    findViewById(R.id.message).setVisibility(View.GONE);
                } else {
                    TextView txt = findViewById(R.id.message);
                    txt.setText(message);
                }
                setCancelable(false);
                Objects.requireNonNull(getWindow()).getAttributes().gravity= Gravity.CENTER;
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.dimAmount=0.7f;
                getWindow().setAttributes(lp);

                //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                show();
            }
        });



        return this;
    }

}
