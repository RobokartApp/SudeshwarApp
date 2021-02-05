package com.ark.robokart_robotics.Fragments.Atl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComponentsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComponentsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComponentsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComponentsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ComponentsFrag newInstance(String param1, String param2) {
        ComponentsFrag fragment = new ComponentsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_components, container, false);
    }
    TextView fragText;
String components;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragText=view.findViewById(R.id.fragText);
        fragText.setText("1. LED\n2. PCB\n3. USB cable");
        int indx=Integer.parseInt(AtlChooseLevel.indx);
        components=AtlChooseLevel.component.get(indx-1);
        String[] strArr=components.split("#");
        String op="";int i=1;
        for(String s:strArr) {
            if(!s.isEmpty()) {
                op += "" + i + ". " + s + "\n";
                i++;
            }
        }
            fragText.setText(op);
    }
}