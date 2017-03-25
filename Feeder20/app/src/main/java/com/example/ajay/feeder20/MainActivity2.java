package com.example.ajay.feeder20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Loginpage.ROLLNUMBER);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Hello"+message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main2);
        layout.addView(textView);
    }
}