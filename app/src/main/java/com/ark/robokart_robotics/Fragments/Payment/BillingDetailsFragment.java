package com.ark.robokart_robotics.Fragments.Payment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.CourseDetails.CourseDetailsActivity;
import com.ark.robokart_robotics.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import carbon.widget.Button;

public class BillingDetailsFragment extends Fragment{

    private static final String TAG = "BillingDetailsFragment";

    Button payment_btn;

    public onClickListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onClickListener) {
            listener = (onClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



    public BillingDetailsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.billing_details_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        payment_btn = view.findViewById(R.id.payment_btn);




        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(true);
            }
        });
    }


    public interface onClickListener{
        void onClick(boolean isClicked);
    }

}
