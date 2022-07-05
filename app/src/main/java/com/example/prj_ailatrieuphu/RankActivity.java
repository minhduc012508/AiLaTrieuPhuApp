package com.example.prj_ailatrieuphu;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import BangXepHangTab.MyViewPagerAdapter;
import InternetProblem.NetworkChangeListener;
import Model.User;

public class RankActivity extends AppCompatActivity {
    MyViewPagerAdapter myViewPagerAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TabLayout tablayoutRank;
    ViewPager2 vpRank;

    ArrayList<User> ds_ranktuan,ds_rankthang,ds_ranknam;
    BackgroundTask backgroundTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Anhxa();
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        vpRank.setAdapter(myViewPagerAdapter);

        new TabLayoutMediator(tablayoutRank, vpRank, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("Trong tuần");
                        break;
                    }
                    case 1:{
                        tab.setText("Trong tháng");
                        break;
                    }
                    case 2:{
                        tab.setText("Trong năm");
                        break;
                    }
                    case 3:{
                        tab.setText("Tùy chọn");
                        break;
                    }
                }
            }
        }).attach();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    private void Anhxa() {
        tablayoutRank = (TabLayout) findViewById(R.id.tablayoutRank);
        vpRank = (ViewPager2) findViewById(R.id.vpRank);
        backgroundTask = new BackgroundTask(this);
      //  ds_ranktuan
    }
}
