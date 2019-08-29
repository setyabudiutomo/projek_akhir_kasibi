package com.navigasi.budi.kasibi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[] dotstv;
    private int[] layouts;
    private Button buttonSkip;
    private Button buttonNext;
    private MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }

        setStatusBarTransparant();

        viewPager = findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        buttonNext = findViewById(R.id.btn_next);
        buttonSkip = findViewById(R.id.btn_skip);

        buttonSkip.setOnClickListener(v -> {
            startMainActivity();
        });

        buttonNext.setOnClickListener(v -> {
            int currentPage = viewPager.getCurrentItem()+1;
            if(currentPage < layouts.length){
                viewPager.setCurrentItem(currentPage);
            }else{
                startMainActivity();
            }
        });

        layouts = new int[] {R.layout.slide_1, R.layout.slide_2, R.layout.slide_3, R.layout.slide_5};
        myPagerAdapter = new MyPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length-1){
                    buttonNext.setText("START");
                    buttonSkip.setVisibility(View.GONE);
                }
                else{
                    buttonNext.setText("NEXT");
                    buttonSkip.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private boolean isFirstTimeStartApp(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSlideApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag", true);
    }

    private void setFirstTimeStartStatus(boolean stt){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSlideApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", stt);
        editor.commit();
    }

    public void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotstv =  new TextView[layouts.length];
        for(int i = 0; i < dotstv.length; i++){
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }

        if(dotstv.length > 0){
            dotstv[page].setTextColor(Color.parseColor("#008577"));
        }
    }

    private void startMainActivity(){
        setFirstTimeStartStatus(true);
        Intent demoIntent = new Intent(this.getApplicationContext(), coba.class);
        demoIntent.putExtra("immersiveMode", "false");
        demoIntent.putExtra("item", "");
        startActivity(demoIntent);
        finish();
    }

    private void setStatusBarTransparant(){
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
