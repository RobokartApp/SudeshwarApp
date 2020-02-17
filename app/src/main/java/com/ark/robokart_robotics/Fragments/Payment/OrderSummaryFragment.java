package com.ark.robokart_robotics.Fragments.Payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.R;

import carbon.widget.Button;

public class OrderSummaryFragment extends Fragment {


    public Button billing_btn;


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

    }


    //Event Listeners
    public void listeners(){

        billing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillingDetailsFragment billingDetailsFragment = new BillingDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_to_left,R.anim.slide_right_to_left,R.anim.slide_right_to_left,R.anim.slide_right_to_left)
                        .replace(R.id.paymentFragment, billingDetailsFragment, "billing")
                        .addToBackStack("order")
                        .commit();
            }
        });

    }
}
