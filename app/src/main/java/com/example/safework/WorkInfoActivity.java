package com.example.safework;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.List;

import ConnectServe.ConnectServe;
import ConnectServe.CompanyInfo;
import ConnectServe.WorkInfo;


public class WorkInfoActivity extends AppCompatActivity {
    private TextView textView;
    private String url = "http://10.0.2.2:8080/CompanyInfo";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏actionbar
        setContentView(R.layout.activity_work_info);
        List<WorkInfo> workInfos =  (List<WorkInfo>) getIntent().getSerializableExtra("workinfos");
        String data = getIntent().getStringExtra("data");
        final WorkInfo workInfo = (WorkInfo)getIntent().getSerializableExtra("workinfo");
        textView = (TextView)findViewById(R.id.workinfo);
        textView.setText(workInfo.getWorkInfo());
        textView = (TextView)findViewById(R.id.worktime);
        textView.setText(workInfo.getWorkDate());
        textView = (TextView)findViewById(R.id.delivertime);
        textView.setText(workInfo.getDeliverDate());
        textView = (TextView)findViewById(R.id.workplace);
        textView.setText(workInfo.getAddress());
        textView = (TextView)findViewById(R.id.worksalary);
        textView.setText(workInfo.getSalary()+"元");
        Button button = (Button)findViewById(R.id.Register);
        //通过workid获取工作信息
        final CompanyInfo companyInfo = new CompanyInfo();
        Handler mHandler = new Handler(){
            JSONArray jsonArray;
            @Override
            public void handleMessage(Message msg) {//覆盖handleMessage方法
                super.handleMessage(msg);
                String data = msg.obj.toString();
                Log.e("e",data+"wwwww");
                try {
                    //取出对象里面的公司信息
                    jsonArray = new JSONArray(data);
                    JSONObject object = jsonArray.getJSONObject(0);
                    companyInfo.setCompanyEmail(object.getString("companyEmail"));
                    companyInfo.setCompanyName(object.getString("companyName"));
                    companyInfo.setCompanyManger(object.getString("companyManger"));
                    companyInfo.setCompanyPhone(object.getString("companyPhone"));
                    textView = (TextView)findViewById(R.id.companyname);
                    textView.setText(companyInfo.getCompanyName());
                    textView = (TextView)findViewById(R.id.companyemail);
                    textView.setText(companyInfo.getCompanyName());
                    textView = (TextView)findViewById(R.id.companyphone);
                    textView.setText(companyInfo.getCompanyPhone());
                    textView = (TextView)findViewById(R.id.companymanager);
                    textView.setText(companyInfo.getCompanyManger());
                }catch (Exception e){

                }
            }
        };
        ConnectServe connectServe = new ConnectServe("http://10.0.2.2:8080/CompanyInfo?companyid=" + workInfo.getCompanyID() ,mHandler);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = getIntent().getStringExtra("username");
                String url = "http://10.0.2.2:8080/addInfo?companyid="+workInfo.getCompanyID() + "&workid=" + workInfo.getWorkID()+"&username=" + username;
                Handler handler =new Handler(){
                    @Override
                    public void handleMessage(Message msg) {//覆盖handleMessage方法
                      super.handleMessage(msg);
                        String data = msg.obj.toString();
                        Toast toast=Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                };
                ConnectServe connectServe1 = new ConnectServe(url,handler);
                //将信息存入数据库
            }
        });
    }
}
