package com.example.twt.myweather;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.twt.myweather.util.JsonParseUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tv_txt;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
          if (msg.what==0){
              String data= (String) msg.obj;
              tv_txt.setText(data);
          }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_json= (Button) findViewById(R.id.btn_json);
        tv_txt= (TextView) findViewById(R.id.tv_txt);
        btn_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJson();

            }
        });
    }

    private void showJson() {
        new Thread(){
            @Override
            public void run() {
                String data=JsonParseUtils.getJsonData();
                Message msg=Message.obtain();
                msg.obj=data;
                msg.what=0;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
}
