package com.example.demobaidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.demobaidumap.MainActivity.jifen;
import static com.example.demobaidumap.HistoryActivity.history_names;
import static com.example.demobaidumap.HistoryActivity.history_icons;

public class ExchangeActivity extends AppCompatActivity {

    private ListView mListView;
    private  String[] names={"苹果13","苹果14","苹果15","苹果16","苹果17","苹果18"};
    private int[] icons={R.drawable.keli,R.drawable.keli,R.drawable.keli,R.drawable.keli,R.drawable.keli,R.drawable.keli};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        final TextView jf_yue = findViewById(R.id.jf_yue);
        jf_yue.setText("当前积分为:"+jifen);
        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(new MyBaseAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(jifen>=10)
                {
                    Toast.makeText(getApplicationContext(), "恭喜兑换成功!", Toast.LENGTH_SHORT).show();
                    jifen-=10;
                    jf_yue.setText("当前积分为:"+jifen);
                    history_names.add(names[position]);
                    history_icons.add(icons[position]);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "当前积分不足!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ExchangeActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names [position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//组装数据
            View view=View.inflate(getApplicationContext(), R.layout.item,null);
            TextView mTextView= (TextView) view.findViewById(R.id.title);
            ImageView imageView= (ImageView) view.findViewById(R.id.image);
            //组件一拿到，开始组装
            mTextView.setText(names[position]);
            imageView.setBackgroundResource(icons[position]);
            //组装玩开始返回
            return view;
        }
    }

}
