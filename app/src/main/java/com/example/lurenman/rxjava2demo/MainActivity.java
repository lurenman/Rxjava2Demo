package com.example.lurenman.rxjava2demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lurenman.rxjava2demo.activity.FlowableActivity;
import com.example.lurenman.rxjava2demo.activity.RxBusActivity;
import com.example.lurenman.rxjava2demo.activity.Rxjava2ActivityOne;
import com.example.lurenman.rxjava2demo.activity.Rxjava2ActivityTwo;

public class MainActivity extends AppCompatActivity {

    private TextView tv_rxjava2One;
    private TextView tv_Flowable;
    private TextView tv_rxjava2Two;
    private TextView tv_RxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_rxjava2One = (TextView) findViewById(R.id.tv_rxjava2One);
        tv_rxjava2Two = (TextView) findViewById(R.id.tv_rxjava2Two);
        tv_Flowable = (TextView) findViewById(R.id.tv_Flowable);
        tv_RxBus = (TextView) findViewById(R.id.tv_RxBus);
        initEvents();

    }

    private void initEvents() {
        tv_rxjava2One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Rxjava2ActivityOne.class);
                startActivity(intent);
            }
        });
        tv_rxjava2Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Rxjava2ActivityTwo.class);
                startActivity(intent);
            }
        });
        tv_Flowable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,FlowableActivity.class);
                startActivity(intent);
            }
        });
        tv_RxBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RxBusActivity.class);
                startActivity(intent);
            }
        });
    }
}
