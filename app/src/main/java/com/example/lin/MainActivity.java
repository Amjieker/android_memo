package com.example.lin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.lin.Tool.Const;
import com.example.lin.Tool.HttpTools;
import com.example.lin.Tool.Tools;

import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends Activity {
    private boolean check_loading, check_res;
    private boolean flag;
    private MyVideoView myVideoView;
    private Date date;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        init();
        date = new Date();
        // 处理背景透明度
//        findViewById(R.id.login_con).getBackground().setAlpha(150);
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

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myVideoView.stopNestedScroll();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(myVideoView !=null) {
            myVideoView.stopPlayback();
        }
        super.onStop();
    }

    public void init(){
        myVideoView = findViewById(R.id.video_view);
        myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.relogin));
        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(mp.isPlaying()){
                    mp.stop();
                    mp.release();
                    mp = new MediaPlayer();
                }
                mp.setVolume(0f,0f);
                mp.setLooping(true);
                mp.start();
            }
        });
        myVideoView.setFocusable(false);
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myVideoView.start();
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
                        .SendPost(Const.Check, object.toString())
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
        // 堵塞执行
        return check_res;
    }

}