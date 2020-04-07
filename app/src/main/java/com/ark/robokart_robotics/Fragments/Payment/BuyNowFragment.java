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
import carbon.widget.TextView;

public class BuyNowFragment extends Fragment {


    Button btn_buy_home;

    TextView home_cost, home_learn_desc, makerspace_cost, makerspace_learn_desc;


    public BuyNowFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buy_now_fragment,container,false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_buy_home = view.findViewById(R.id.btn_buy_home);

        home_cost = view.findViewById(R.id.home_cost);
        home_learn_desc = view.findViewById(R.id.home_learn_desc);
        makerspace_cost = view.findViewById(R.id.makerspace_cost);
        makerspace_learn_desc = view.findViewById(R.id.makerspace_learn_desc);

        String h_cost = getArguments().getString("home_cost");
        home_cost.setText("₹ "+h_cost);//set string over textview
        String h_desc = getArguments().getString("home_desc");
        home_learn_desc.setText(h_desc);
        String m_cost = getArguments().getString("makerspace_cost");
        makerspace_cost.setText("₹ "+m_cost);
        String m_desc = getArguments().getString("makerspace_learn_desc");
        makerspace_learn_desc.setText(m_desc);


        btn_buy_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                        .replace(R.id.paymentFragment, orderSummaryFragment, "order")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
