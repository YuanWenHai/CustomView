package com.will.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.will.customview.view.circleloading.CircleCountdownView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleCountdownView view = (CircleCountdownView) findViewById(R.id.view);
        view.setCountdownTime(10);
        view.start();
        view.setCountdownFinishListener(new CircleCountdownView.OnCountdownFinishListener() {
            @Override
            public void onFinish() { 
                Toast.makeText(MainActivity.this, "countdown finished!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
