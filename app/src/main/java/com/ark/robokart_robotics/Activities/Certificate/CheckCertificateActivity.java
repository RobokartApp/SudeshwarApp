package com.ark.robokart_robotics.Activities.Certificate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ark.robokart_robotics.R;

public class CheckCertificateActivity extends AppCompatActivity {

    private EditText edt_cert_id;

    private TextView message;

    private Button check_btn, download_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_certificate);

        init();

        listeners();

    }

    public void init(){
        edt_cert_id = findViewById(R.id.edt_cert_id);
        message = findViewById(R.id.message);
        check_btn = findViewById(R.id.check_btn);
        download_btn = findViewById(R.id.download_btn);
    }

    public void listeners(){

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_cert_id.getText().toString().trim().equals("123456")){
                    message.setVisibility(View.VISIBLE);
                    message.setText("This certificate is issued to Charul Dalvi  in Brain X - Arduino Based (English)");
                    message.setTextColor(getResources().getColor(R.color.carbon_green_600));
                    download_btn.setVisibility(View.VISIBLE);
                }
                else{
                    message.setVisibility(View.VISIBLE);
                    message.setText("Invalid Certificate ID");
                    message.setTextColor(getResources().getColor(R.color.red));
                    download_btn.setVisibility(View.GONE);
                }
            }
        });


    }
}
