package com.ark.robokart_robotics.Fragments.AskDoubt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ark.robokart_robotics.Activities.AskDoubt.CreateDoubtAct;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.PostAdapter;
import com.ark.robokart_robotics.Model.MyPostModel;
import com.ark.robokart_robotics.R;

import java.util.List;

public class AskDoubtFragment extends Fragment{

    public AskDoubtFragment() {
        // Required empty public constructor
    }

    private RecyclerView postRecyclerview;

    private PostViewModel postViewModel;

    private PostAdapter postAdapter;
    TextView new_doubt;
    static ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;

    static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ask_doubt, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Menu menu= HomeActivity.bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(3);
        menuItem.setChecked(true);

        context=getActivity();

        postViewModel =  new ViewModelProvider(this).get(PostViewModel.class);

        init(view);

        listeners();
    }

    public void init(View v){

        //animationView = v.findViewById(R.id.animationView);

        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange);
        progressBar=v.findViewById(R.id.progressBar);
        postRecyclerview = v.findViewById(R.id.recycler_post);
        new_doubt=v.findViewById(R.id.tv_NewDoubt);

        postViewModel.getMyPostsList().observe(getActivity(), new Observer<List<MyPostModel>>() {
            @Override
            public void onChanged(List<MyPostModel> postListModels) {
                postAdapter = new PostAdapter(getContext(),postListModels);
                postRecyclerview.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }
        });
    }
    private void listeners() {
        new_doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), CreateDoubtAct.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shuffle();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void shuffle() {

        Intent intent=new Intent(getContext(),HomeActivity.class);
        intent.putExtra("post","ok");
        startActivity(intent);
        getActivity().finish();
    }


}