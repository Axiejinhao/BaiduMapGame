package com.example.demobaidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {
    public static ArrayList<String> history_names = new ArrayList<String>();
    public static ArrayList<Integer> history_icons = new ArrayList<Integer>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ListView history_ListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        history_ListView = (ListView)findViewById(R.id.listView_history);
        history_ListView.setAdapter(new MyBaseAdapter());
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return history_names.size();
        }

        @Override
        public Object getItem(int position) {
            return history_names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//组装数据
            View view=View.inflate(getApplicationContext(), R.layout.history_item,null);
            TextView mTextView= (TextView) view.findViewById(R.id.title);
            ImageView imageView= (ImageView) view.findViewById(R.id.image);
            TextView Today_time= (TextView) view.findViewById(R.id.today_time);
            //组件一拿到，开始组装
            mTextView.setText(history_names.get(position));
            imageView.setBackgroundResource(history_icons.get(position));

            Date curDate =  new Date(System.currentTimeMillis());
            //获取当前时间
            String str =  formatter.format(curDate);
            Today_time.setText(str);
            //组装玩开始返回
            return view;
        }
    }

}
