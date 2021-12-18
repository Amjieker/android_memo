package com.example.lin;

import android.content.Context;
import android.widget.Toast;

/*
封装一点工具
Toast 写得太tm长了
 */
public class Tools {
    public static void Toast(Context context,String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
