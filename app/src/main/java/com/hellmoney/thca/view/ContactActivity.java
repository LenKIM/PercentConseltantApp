package com.hellmoney.thca.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.hellmoney.thca.R;

import java.net.URISyntaxException;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView call;
    private ImageView kakaoTalk;

    public static boolean isInstalledApp(Context context, String packageName) {
        List<PackageInfo> packageList = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for(PackageInfo info : packageList){
            if(info != null && info.packageName != null && info.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        call = (ImageView) findViewById(R.id.call);
        kakaoTalk = (ImageView) findViewById(R.id.kakaoTalk);
        //RecyclerView에 LayoutManager 설정 및 adapter 설정

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("공지사항");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        setSupportActionBar(toolbar); //Toolbar를 현재 Activity의 Actionbar로 설정.
//
//        //Toolbar 설정
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }

//        toolbar.setTitle(getResources().getString(R.string.contact));
//        toolbar.setTitleTextColor(ResourcesCompat.getColor(getApplicationContext().getResources(), R.color.normalTypo, null));

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:070-8237-8282"));
                startActivity(intent);
            }
        });

        kakaoTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String KAKAOTALK_URI = "com.kakao.talk";

                if(isInstalledApp(ContactActivity.this, KAKAOTALK_URI)) {
                    try {
                        startActivity(Intent.parseUri("intent://plusfriend/friend/@hellomoney#Intent;scheme=kakaoplus;package=com.kakao.talk;end", Intent.URI_INTENT_SCHEME ));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + KAKAOTALK_URI));
                    startActivity(intent);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
