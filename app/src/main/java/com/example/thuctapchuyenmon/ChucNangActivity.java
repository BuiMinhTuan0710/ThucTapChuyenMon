package com.example.thuctapchuyenmon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.adapter.ViewPagerNavigationAdapter;
import com.example.database.connect;
import com.example.model.DialogLoading;
import com.example.model.ExploreFragment;
import com.example.model.MyOrderFlagment;
import com.example.model.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ChucNangActivity extends AppCompatActivity {
    private ViewPager viewPager;
    public static BottomNavigationView navigation;
    private MenuItem prevMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuc_nang);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewNavigation);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }

                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Intent intent = getIntent();
        String makh = intent.getStringExtra("makh");
        Log.e("makhachhang",makh );
        ViewPagerNavigationAdapter adapter = new ViewPagerNavigationAdapter(getSupportFragmentManager());
        ExploreFragment exploreFragment = new ExploreFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        MyOrderFlagment myOrderFlagment = new MyOrderFlagment();
        exploreFragment.MaKhachHang = makh ;
        profileFragment.makh =makh;
        myOrderFlagment.MaKhachHang = makh;
        adapter.addFragment(exploreFragment);
        adapter.addFragment(myOrderFlagment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_explore:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_my_order:
                    viewPager.setCurrentItem(1);

                    return true;
                case R.id.navigation_favourite:
                    viewPager.setCurrentItem(2);

                    return true;
                case R.id.navigation_profile:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

}
