package com.example.lin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends Activity {
    private boolean check_loading, check_res;
    private boolean flag;
    private Date date;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        date = new Date();
        // 处理背景透明度
        findViewById(R.id.login_con).getBackground().setAlpha(150);
        // 登录监听
        flag = true;
        findViewById(R.id.button_login).setOnClickListener(V -> {
            // 防抖措施，一秒钟只能点一次
            if (new Date().getTime() - date.getTime() >= 1000) {
                date = new Date();
                String str = ((TextView) findViewById(R.id.login_account)).getText().toString();
                if (!str.equals("")) {
                    if (check()) {
                        Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                        intent.putExtra("account", str);
                        startActivity(intent);
                        Tools.Toast(this, "登录成功");
                    } else
                        Tools.Toast(this, "账号不存在或密码错误");
                } else
                    Tools.Toast(this, "登录输入账号");
            } else
                Tools.Toast(this, "亲，点慢点哦");
        });
        findViewById(R.id.button_reg).setOnClickListener(V -> {
            if (new Date().getTime() - date.getTime() >= 1000) {
                date = new Date();
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                startActivity(intent);
            } else {
                Tools.Toast(this, "亲，点慢点哦");
            }
        });
    }

    public synchronized boolean check() {
        check_loading = true;
        check_res = false;
        new Thread(() -> {
            try {
                String nick = ((TextView) findViewById(R.id.login_account)).getText().toString();
                String pass = ((TextView) findViewById(R.id.login_pass)).getText().toString();
                JSONObject object = new JSONObject();
                object.accumulate("nick", nick);
                object.accumulate("pass", pass);
                check_res = new JSONObject(HttpTools
                        .SendPost("http://101.43.66.34:8084/check", object.toString())
                ).get("code")
                        .equals("yes");
                System.out.println(check_res);
            } catch (Exception e) {
                check_res = false;
                e.printStackTrace();
            }
            check_loading = false;
        }).start();
        int x = 0;
        /*
         * cao 不加sout这句三次请求直接崩，加了反而正常跑？
         * bug 先留着，好像影响不大，内存泄漏好像没有
         * */
        while (check_loading) System.out.println("thread :" + x);
        ; // 堵塞执行
        return check_res;
    }

}