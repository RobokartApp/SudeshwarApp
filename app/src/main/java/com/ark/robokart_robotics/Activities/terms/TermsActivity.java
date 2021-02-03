package com.ark.robokart_robotics.Activities.terms;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ark.robokart_robotics.R;

public class TermsActivity extends AppCompatActivity {
WebView terms;
ImageView back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        back_arrow = findViewById(R.id.back_btn);

        terms=findViewById(R.id.term_webview);
        terms.loadUrl("https://robokart.com/appTerms");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}