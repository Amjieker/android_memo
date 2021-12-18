package com.example.lin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;

public class RegActivity extends Activity {
    private Context context;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            // 接受消息
            if (msg.what == 0x211){
                System.out.println(111);
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_reg);
        // 设置一下透明°
        findViewById(R.id.reg_con).getBackground().setAlpha(150);
        /*
            注册 没有校验，不完善，后续填坑
            直接跳转
         */
        findViewById(R.id.button_reg).setOnClickListener(V -> {
            String nick = ((TextView) findViewById(R.id.reg_account)).getText().toString();
            String pass = ((TextView) findViewById(R.id.reg_pass)).getText().toString();
            if (nick.equals("") || pass.equals("")) Tools.Toast(this, "昵称和账号必须输");
            else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject json = new JSONObject();
                        try {
                            json.accumulate("nick", nick);
                            json.accumulate("pass", pass);
                            boolean ans = new JSONObject(HttpTools
                                    .SendPost("http://101.43.66.34:8084/add", json.toString()))
                                    .get("code")
                                    .equals("yes");
                            Looper.prepare();
                            Tools.Toast(RegActivity.this, ans
                                    ? "注册成功~" : "重复注册或遇见错误啦"
                            );
                            if (ans) {
                                System.out.println(0x211);
                                handler.sendEmptyMessage(0x211);
                            }
                            Looper.loop();
                        } catch (Exception e) {
                            System.out.println("这儿抛了个异常。。。");
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}