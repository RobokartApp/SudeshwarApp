package com.ark.robokart_robotics.Fragments.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.R;

import carbon.widget.Button;
import carbon.widget.TextView;

public class OrderSummaryFragment extends Fragment {


    public Button billing_btn;

    ImageButton minus_btn, plus_btn;

    TextView quantity_txt, price_edt;

    public  int quantity_count = 1;


    public OrderSummaryFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ordersummaryfragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        listeners();
    }

    //Initialise UI
    public void init(View v){

        billing_btn = v.findViewById(R.id.billing_btn);

        minus_btn = v.findViewById(R.id.minus_btn);
        plus_btn = v.findViewById(R.id.plus_btn);
        quantity_txt = v.findViewById(R.id.quantity_txt);
        price_edt = v.findViewById(R.id.price);
    }


    //Event Listeners
    public void listeners(){

        billing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillingDetailsFragment billingDetailsFragment = new BillingDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                        .replace(R.id.paymentFragment, billingDetailsFragment, "billing")
                        .addToBackStack("order")
                        .commit();
            }
        });


        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementQuantity();
                quantity_txt.setText(String.valueOf(quantity_count));
//                int price = Integer.parseInt(price_edt.getText().toString());
//                int val = price * quantity_count;
//                price_edt.setText(String.valueOf(val));

            }
        });

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decerementQuantity();
                quantity_txt.setText(String.valueOf(quantity_count));
//                int price = Integer.parseInt(price_edt.getText().toString());
//                int val = price * quantity_count;
//                price_edt.setText(String.valueOf(val));
            }
        });

    }


    public void incrementQuantity(){
        quantity_count++;
    }

    public void decerementQuantity(){
        if(quantity_count != 0)
        quantity_count--;
    }
}
