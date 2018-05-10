package com.example.safework;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import ConnectServe.ConnectServe;


public class LoginActivity extends AppCompatActivity {
    private EditText userText;
    private EditText passTex;
    private Button register;
    private Button login;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        //获取编辑框
        userText = (EditText)findViewById(R.id.login_edit_account);
        passTex = (EditText)findViewById(R.id.login_edit_pwd);
        //获取两个登陆和注册的按钮
        register = (Button)findViewById(R.id.login_btn_register);
        login = (Button)findViewById(R.id.login_btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进行登陆检测
                final String username = userText.getText().toString();
                String password = passTex.getText().toString();
                String url = "http://10.0.2.2:8080/login?username=" + username + "&password=" + password ;
                Handler mHandler = new Handler(){
                    JSONArray jsonArray;
                    @Override
                    public void handleMessage(Message msg) {//覆盖handleMessage方法
                        super.handleMessage(msg);
                        String data = msg.obj.toString();
                        if(data.equals("IsUser")){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                        }else{
                            Toast toast=Toast.makeText(getApplicationContext(), "用户名或者密码错误", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                ConnectServe connectServe = new ConnectServe(url ,mHandler);
            }
        });
    }
}
