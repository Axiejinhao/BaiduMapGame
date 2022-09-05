package com.example.demobaidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.uuzuche.lib_zxing.activity.CodeUtils;

public class QRcodeActivity extends AppCompatActivity {

    private EditText sou;
    private Button btn;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        //找控件
        sou = findViewById(R.id.sou);
        btn = findViewById(R.id.btn);
        img = findViewById(R.id.img);
        //输入内容后点击搜索生成一个二维码
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = sou.getText().toString();

                if (TextUtils.isEmpty(s)){
                    return;
                }
                Bitmap bitmap = CodeUtils.createImage(s, 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.keli));
                img.setImageBitmap(bitmap);
            }
        });
    }
}
