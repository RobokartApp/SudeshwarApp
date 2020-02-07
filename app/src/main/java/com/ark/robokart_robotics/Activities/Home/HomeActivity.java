package com.ark.robokart_robotics.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ark.robokart_robotics.Fragments.DashboardFragment;
import com.ark.robokart_robotics.R;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import carbon.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingRootNav slidingRootNav;

    private ImageView drawer_btn, back_arrow;

    private TextView name;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer_btn = findViewById(R.id.drawer_btn);

        fragmentManager = getSupportFragmentManager();


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withGravity(SlideGravity.LEFT)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        name = findViewById(R.id.name);

        back_arrow = findViewById(R.id.back_arrow);

        DashboardFragment dashboardFragment = new DashboardFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainFrameLayout,dashboardFragment,"dashboard");
        transaction.addToBackStack(null);
        transaction.commit();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu(true);
            }
        });


        drawer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.openMenu();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }


}
