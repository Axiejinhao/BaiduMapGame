package com.example.demobaidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int tili=90;
    public static int jifen=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView sum_tili = findViewById(R.id.sum_tili);

        final TextView sum_jifen = findViewById(R.id.sum_jifen);

        Button get_item = findViewById(R.id.get_item);
        get_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sum_jifen.setText("当前积分 "+jifen);
                sum_tili.setText("可用体力 "+tili);
            }

        });

        Button playgame = findViewById(R.id.playgame);
        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }

        });

        Button shop = findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ExchangeActivity.class);
                startActivity(intent);
            }

        });

        Button scan = findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });

        Button create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,QRcodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
