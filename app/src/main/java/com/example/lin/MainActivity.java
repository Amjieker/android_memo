package com.example.lin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 处理背景透明度
        findViewById(R.id.login_con).getBackground().setAlpha(150);

        findViewById(R.id.button_login).setOnClickListener(V -> {
            String str = ((TextView)findViewById(R.id.login_account)).getText().toString();
            if(!str.equals("")){
                Intent intent = new Intent(MainActivity.this,MsgActivity.class);
                intent.putExtra("account",str);
                startActivity(intent);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "登录输入账号", Toast.LENGTH_SHORT).show();
            }
        });


    }
    
}