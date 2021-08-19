package com.ark.robokart_robotics.Fragments.Payment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.CourseDetails.CourseDetailsActivity;
import com.ark.robokart_robotics.R;

import carbon.widget.Button;

public class BillingDetailsFragment extends Fragment{

    private static final String TAG = "BillingDetailsFragment";

    Button payment_btn;

    public onClickListener listener;
    EditText Ename,Emobile,Eemail,Epincode,Elandmark,Eaddress,Estate,Ecity;
    String name,mobile,email,pincode,landmark,address,state,city;

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
        Ename=view.findViewById(R.id.fullname_edt_txt);
        Emobile=view.findViewById(R.id.mobile_edt_txt);
        Eemail=view.findViewById(R.id.email_edt_txt);
        Epincode=view.findViewById(R.id.pincode_edt_txt);
        Eaddress=view.findViewById(R.id.address_edt_txt);
        Estate=view.findViewById(R.id.state_edt_txt);
        Ecity=view.findViewById(R.id.city_edt_txt);
        Elandmark=view.findViewById(R.id.street_edt_txt);

        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = Ename.getText().toString();
                mobile = Emobile.getText().toString();
                email = Eemail.getText().toString();
                pincode = Epincode.getText().toString();
                address = Eaddress.getText().toString();
                state = Estate.getText().toString();
                city = Ecity.getText().toString();
                landmark =Elandmark.getText().toString();
                if(name==""||name.isEmpty()) {
                    Ename.setError("Enter Name");Ename.requestFocus();
                }
                else if (mobile==""||mobile.isEmpty()){
                    Emobile.setError("Enter Mobile");Emobile.requestFocus();
                }
                else if (email==""||email.isEmpty()) {
                    Eemail.setError("Enter Email");Eemail.requestFocus();
                }
                else if (pincode==""||pincode.isEmpty()) {
                    Epincode.setError("Enter Pincode");Epincode.requestFocus();
                }
                else if (address==""||address.isEmpty()) {
                    Eaddress.setError("Enter Address");Eaddress.requestFocus();
                }
                else if (landmark==""||landmark.isEmpty()) {
                    Elandmark.setError("Enter Landmark");Elandmark.requestFocus();
                }
                else if (city ==""||city.isEmpty()) {
                    Ecity.setError("Enter City");Ecity.requestFocus();
                }
                else if (state==""||state.isEmpty()) {
                    Estate.setError("Enter State");
                    Estate.requestFocus();
                }else {
                    listener.onClick(true);
                    CourseDetailsActivity.name = name;
                    CourseDetailsActivity.mobile = mobile;
                    CourseDetailsActivity.email = email;
                    CourseDetailsActivity.pincode = pincode;
                    CourseDetailsActivity.address = address;
                    CourseDetailsActivity.state = state;
                    CourseDetailsActivity.city = city;
                    CourseDetailsActivity.landmark = landmark;
                }
            }
        });
    }


    public interface onClickListener{
        void onClick(boolean isClicked);
    }

}
