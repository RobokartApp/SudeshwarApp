package com.ark.robokart_robotics.Activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Adapters.NData;
import com.ark.robokart_robotics.Adapters.NotAdapter;
import com.ark.robokart_robotics.DBHelper;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

public class NotifyAct extends AppCompatActivity {
    public  static RecyclerView recyclerView;
    private NotAdapter adapter;
    private List<NData> movieList = new ArrayList<>();
    DBHelper dbHelper;
    private SQLiteDatabase database;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        {

          /*  if (MainActivity.nt.getVisibility() == View.VISIBLE) {
                MainActivity.nt.setVisibility(View.GONE);
            }*/
            recyclerView=findViewById(R.id.recycler_view);
            adapter = new NotAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        dbHelper = new DBHelper(getApplicationContext());
        database = dbHelper.getWritableDatabase();

        Cursor cc=fetch();
        if(cc!=null) {
            //cc.moveToFirst();
            cc.moveToLast();
            for (int i = 0; i < cc.getCount(); i++) {
                NData movie = new NData(cc.getString(1), cc.getString(2),cc.getString(3));
                movieList.add(movie);
                cc.moveToPrevious();
            }
        }
        adapter.notifyDataSetChanged();

        back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyAct.super.onBackPressed();
            }
        });
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBHelper._ID, DBHelper.TITLE, DBHelper.BODY, DBHelper.TIME };
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            this.finish();
        return true;
    }


}
