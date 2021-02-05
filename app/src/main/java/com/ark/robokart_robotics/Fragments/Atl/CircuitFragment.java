package com.ark.robokart_robotics.Fragments.Atl;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ark.robokart_robotics.Activities.AtlChooseLevel.AtlChooseLevel;
import com.ark.robokart_robotics.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

public class CircuitFragment extends Fragment {


    public CircuitFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CircuitFragment newInstance(String param1, String param2) {
        CircuitFragment fragment = new CircuitFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
PhotoView circuit;
    ProgressBar progressBar;

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_circuit, container, false);
    }
String id,c;int indx;
    private float mScaleFactor = 1.0f;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circuit=view.findViewById(R.id.circuit_img);
        indx=Integer.parseInt(AtlChooseLevel.indx);
        progressBar=view.findViewById(R.id.progressBar);

                c = AtlChooseLevel.circuit.get(indx - 1);
        String[] sArr=c.split("/");
        id=sArr[5];//"1aNjZyQ1Eeb3guDE9x8ca0OiVm_JVzJqC";
        Glide.with(getActivity()).load("https://drive.google.com/uc?id="+id)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(circuit);
    }

}