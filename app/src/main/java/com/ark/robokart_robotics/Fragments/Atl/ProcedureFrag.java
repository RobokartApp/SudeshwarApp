package com.ark.robokart_robotics.Fragments.Atl;

import android.graphics.text.LineBreaker;
import android.os.Build;
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

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;


public class ProcedureFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProcedureFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProcedureFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProcedureFrag newInstance(String param1, String param2) {
        ProcedureFrag fragment = new ProcedureFrag();
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
        return inflater.inflate(R.layout.fragment_procedure, container, false);
    }
    TextView fragText;
String procedure;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragText=view.findViewById(R.id.fragText);
        /*String[] sArr=new String[111];
        sArr[0] ="- Step 1: This is step one.";
         sArr[1] ="\n- Step 2: This is step two.";
         String string="";
        for(int i=0;i<=1;i++)
            string+=sArr[i];
        fragText.setText(""+string);*/
        int indx=Integer.parseInt(AtlChooseLevel.indx);
        procedure=AtlChooseLevel.procedure.get(indx-1);
        String[] strArr=procedure.split("#");
        String op="";int i=1;
        for(String s:strArr) {
            if(!s.isEmpty()) {
                op += " " + i + ". " + s + "\n\n";
                i++;
            }
        }
        fragText.setText(op);

    }
}