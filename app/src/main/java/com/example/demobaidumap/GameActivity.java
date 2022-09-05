package com.example.demobaidumap;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.demobaidumap.MainActivity.jifen;
import static com.example.demobaidumap.MainActivity.tili;

public class GameActivity extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;

    boolean isFirstLoc = true; // 是否首次定位
    public UiSettings mUiSettings;
    public TextView jd;
    public TextView wd;

    public double latitude;    //纬度信息
    public double longitude;    //经度信息

    Random random = new Random();

    public int bao_sum=12; //宝箱总数

    ArrayList<Marker> markers = new ArrayList();

    MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_game);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setTrafficEnabled(true);

        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(true);
        //通过设置enable为true或false 选择是否显示指南针
        mUiSettings.setCompassEnabled(true);
        mBaiduMap.setCompassPosition(new Point(100,200));
        //通过设置enable为true或false 选择是否启用地图俯视功能
        mUiSettings.setOverlookingGesturesEnabled(true);
        //通过设置enable为true或false 选择是否启用地图旋转功能
        mUiSettings.setRotateGesturesEnabled(true);
        //是否双击放大当前地图中心点 默认：false 即按照双击位置放大地图
        mUiSettings.setEnlargeCenterWithDoubleClickEnable(true);

        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                BitmapDescriptorFactory.fromResource(R.drawable.keli),
                0xAAFFFF88,
                0xAA00FF00);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);

        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        RandomLocationListener randomLocationListener = new RandomLocationListener();
        JudgmentListener judgmentListener = new JudgmentListener();
        TiliTimeListener tiliTimeListener = new TiliTimeListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.registerLocationListener(randomLocationListener);
        mLocationClient.registerLocationListener(judgmentListener);
        mLocationClient.registerLocationListener(tiliTimeListener);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启地图定位图层
                mLocationClient.start();
            }

        });
    }

    //获取坐标并生成位置图标
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            //体力进度条
            ProgressBar progressBar = findViewById(R.id.progressbar);
            progressBar.setProgress(tili);

            //体力数值
            TextView tili_tv = findViewById(R.id.tili_tv);
            tili_tv.setText(""+progressBar.getProgress());

            //积分显示
            TextView jifen_tv = findViewById(R.id.jifen);
            jifen_tv.setText("积分:"+jifen);

        }
    }

    //随机生成宝藏
    public class RandomLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            jd = findViewById(R.id.jd);
            wd = findViewById(R.id.wd);
            jd.setText("经度:"+longitude);
            wd.setText("纬度:"+latitude);

            int i=0;
            while(markers.size()<bao_sum)
            {
                double x=(random.nextDouble())*0.01;
                double y=(random.nextDouble())*0.01;
                if(i%4==0)
                {
                    x*=-1;
                    y*=-1;
                }
                else if(i%4==1)
                {
                    x*=1;
                    y*=-1;
                }
                else if(i%4==2)
                {
                    x*=-1;
                    y*=1;
                }
                //定义Maker坐标点
                LatLng point = new LatLng(latitude+x, longitude+y);
                //构建Marker图标
                int bao;
                bao = random.nextInt(100);
                BitmapDescriptor bitmap ;
                String title;
                if(bao >=80)
                {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.bao3);
                    title="bao3";
                }
                else if(bao >=60)
                {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.bao2);
                    title="bao2";
                }
                else
                {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.bao1);
                    title="bao1";
                }
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions overlayOption = new MarkerOptions()
                        .position(point)
                        .icon(bitmap)
                        .title(title);
                Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOption));
                markers.add(marker);
                i++;
                if(i==104)
                {
                    i=0;
                }
            }

        }
    }

    //判断是否寻到宝藏
    public class JudgmentListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息

            double bao_lati;
            double bao_longi;
            boolean flag;
            Marker marker_moster;
            for(int j=0;j<markers.size();j++)
            {
                Marker marker = markers.get(j);
                bao_lati = marker.getPosition().latitude;
                bao_longi = marker.getPosition().longitude;
                if(Math.abs(bao_lati-latitude)<=0.00008 && Math.abs(bao_longi-longitude)<=0.00008)
                {
                    flag = Monster(location,marker);
                    if(flag)
                    {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.dadada);
                        mediaPlayer.start();
                        Toast.makeText(getApplicationContext(), "找到宝藏啦!!!", Toast.LENGTH_SHORT).show();    //屏幕下方的弹窗
                        marker.remove();
                        markers.remove(marker);
                        int rand=random.nextInt(100);
                        if(rand%2==0)
                        {
                            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.bao1);
                            mediaPlayer.start();
                        }
                        else
                        {
                            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.bao2);
                            mediaPlayer.start();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "找到宝藏啦!!!\n可惜体力不足", Toast.LENGTH_SHORT).show();    //屏幕下方的弹窗
                    }
                }
            }

        }
    }

    //体力恢复计时
    public class TiliTimeListener extends BDAbstractLocationListener {
        Timer timer = new Timer();// 实例化Timer类
        TextView tili_time = findViewById(R.id.tili_time);
        int time_sum=480;
        @Override
        public void onReceiveLocation(BDLocation location){
            int hour=0,minute=0;
            if(tili<100)
            {
                if(time_sum>0)
                {
                    timer.schedule(new TimerTask() {
                        public void run() {
                        }
                    }, 1000);
                    time_sum--;
                    hour=time_sum/60;
                    minute=time_sum-60*hour;
                    tili_time.setText("距离下此体力恢复还剩: "+hour+":"+minute);
                    if(time_sum==0)
                    {
                        tili++;
                        time_sum=480;
                    }
                }
            }
        }
    }

    //生成怪物
    public boolean Monster (BDLocation location,Marker marker_it) {
        latitude = location.getLatitude();    //获取纬度信息
        longitude = location.getLongitude();    //获取经度信息
        LatLng point = new LatLng(latitude, longitude);
        int rand = random.nextInt(100);
        int need; //需要的体力
        BitmapDescriptor bitmap_it;
        int beishu; //获得积分的倍数
        if(rand >= 80)
        {
            bitmap_it = BitmapDescriptorFactory
                .fromResource(R.drawable.boss3);
            need=30;
            beishu=3;
        }
        else if(rand >= 50)
        {
            bitmap_it = BitmapDescriptorFactory
                    .fromResource(R.drawable.boss2);
            need=20;
            beishu=2;
        }
        else
        {
            bitmap_it = BitmapDescriptorFactory
                    .fromResource(R.drawable.boss1);
            need=10;
            beishu=1;
        }

        //宝箱倍数
        if(marker_it.getTitle().equals("bao3"))
        {
            beishu*=3;
        }
        else if(marker_it.getTitle().equals("bao2"))
        {
            beishu*=2;
        }

        OverlayOptions overlayOption = new MarkerOptions()
                .position(point)
                .icon(bitmap_it);
        final Marker marker_moster = (Marker) (mBaiduMap.addOverlay(overlayOption));
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                marker_moster.remove();
            }
        }, 2000);// 这里百毫秒
        if(tili>=need)
        {
            tili-=need;
            jifen+=beishu*10;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        super.onDestroy();
    }
}
