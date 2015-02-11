package com.twnel.easylink;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button butChatNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butChatNow = (Button) findViewById(R.id.but_chat);
        butChatNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //start navigation to easytaxi chat room in Twnel app
        TwnelUtils.navigateToChat(MainActivity.this, "easytaxi", "EASYTAXI", "com.twnel.easylink", "com.twnel.easylink.MainActivity");
    }
}
