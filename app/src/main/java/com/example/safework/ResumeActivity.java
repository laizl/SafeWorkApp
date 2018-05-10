package com.example.safework;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

import ConnectServe.CompanyInfo;
import ConnectServe.ConnectServe;


public class ResumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        String userinfoid = getIntent().getStringExtra("userinfoid");
        List<CompanyInfo> companyInfos = new ArrayList<CompanyInfo>();
        Handler mHandler = new Handler(){
            JSONArray jsonArray;
            @Override
            public void handleMessage(Message msg) {//覆盖handleMessage方法
                super.handleMessage(msg);
                String data = msg.obj.toString();
            }
        };
        ConnectServe connectServe = new ConnectServe("e",mHandler);
    }
}
