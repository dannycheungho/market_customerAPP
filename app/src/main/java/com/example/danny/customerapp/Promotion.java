package com.example.danny.customerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Promotion extends AppCompatActivity {
    private Button Profile;
    private TextView textView;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

}