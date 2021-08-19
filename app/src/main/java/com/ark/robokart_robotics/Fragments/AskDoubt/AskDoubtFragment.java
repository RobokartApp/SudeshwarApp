package com.ark.robokart_robotics.Fragments.AskDoubt;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ark.robokart_robotics.Activities.AskDoubt.CreateDoubtAct;
import com.ark.robokart_robotics.Activities.AskDoubt.DoubtAllComment;
import com.ark.robokart_robotics.Activities.Home.HomeActivity;
import com.ark.robokart_robotics.Adapters.PostAdapter;
import com.ark.robokart_robotics.Fragments.Stories.VideoItem;
import com.ark.robokart_robotics.Model.MyPostModel;
import com.ark.robokart_robotics.R;

import java.util.List;
import java.util.Objects;

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
    List<MyPostModel> data_list;

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
        MenuItem menuItem=menu.getItem(4);
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
                data_list=postListModels;
                postAdapter = new PostAdapter(getContext(), postListModels, new PostAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int positon, String postID, String post_doubt, View view) {
                        //Toast.makeText(getContext(), "ID:"+postID+"&postDoubt:"+post_doubt, Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getContext(), DoubtAllComment.class);
                        int flags = intent.getFlags();
                        intent.putExtra("post_id",postID);
                        intent.putExtra("post_ques",post_doubt);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        // startActivityForResult(intent,positon);
                        if (intent.resolveActivity(context.getPackageManager()).getPackageName().equals("com.ark.robokart_robotics"))
                            startActivityForResult(intent,positon);

                    }
                });
                postRecyclerview.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // TODO Extract the data returned from the child Activity.
            String returnValue = data.getStringExtra("comment");
            //Toast.makeText(context, ""+returnValue, Toast.LENGTH_SHORT).show();

            MyPostModel item=data_list.get(requestCode);
            String new_comment;
            if (returnValue.equals("minus"))
                new_comment=""+(Integer.parseInt(item.getPost_comment())-1);
            else
                new_comment=""+(Integer.parseInt(item.getPost_comment())+1);
            item.setPost_comment(new_comment);
            data_list.remove(requestCode);
            data_list.add(requestCode,item);
            postAdapter.notifyDataSetChanged();
        }


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