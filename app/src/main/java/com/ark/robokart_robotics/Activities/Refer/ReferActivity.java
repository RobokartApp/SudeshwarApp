package com.ark.robokart_robotics.Activities.Refer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class ReferActivity extends AppCompatActivity {

    TextView createlink,invited_users;
    String cust_id;
    ImageView back_ic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        createlink = (TextView) findViewById(R.id.createlink);
        invited_users=findViewById(R.id.invited_users);
        invited_users.setText("Invited Users : "+HomeActivity.invited_users);

        SharedPreferences sharedPreferences = getSharedPreferences("userdetails",Context.MODE_PRIVATE);
        cust_id = sharedPreferences.getString("customer_id","848");

        createlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createlink();
            }
        });

        back_ic=findViewById(R.id.back_btn);

        back_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferActivity.super.onBackPressed();
            }
        });
    }
    public void createlink(){
        Log.e("main", "create link ");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://robokart.com/App?by="+cust_id))
                .setDynamicLinkDomain("robokart.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                //.setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
//click -- link -- google play store -- inistalled/ or not  ----
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "  Long refer "+ dynamicLink.getUri());
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link
        // manuall link
        String sharelinktext  = "https://robokart.page.link/?"+
                "link=https://robokart.com/App?by="+cust_id+
                "&apn="+ getPackageName()+
                "&st="+"My Refer Link"+
                "&sd="+"Reward Coins 20"+
                "&si="+"https://play-lh.googleusercontent.com/7Ed9yb154A8MiLE1R-lRpHBshff_TOxwyFQFGCnIAJlbDBR_NmvIpdrklGZe-Y39r2I=s150-rw";
        // shorten the link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                //.setLongLink(dynamicLink.getUri())
                .setLongLink(Uri.parse(sharelinktext))  // manually
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main ", "short link "+ shortLink.toString());
                            // share app dialog
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        } else {
                            // Error
                            // ...
                            Log.e("main", " error "+task.getException() );
                        }
                    }
                });
    }
}