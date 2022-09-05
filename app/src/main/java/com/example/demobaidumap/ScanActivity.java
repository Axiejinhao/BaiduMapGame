package com.example.demobaidumap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.annotation.RequiresApi;
import android.Manifest;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

public class ScanActivity extends AppCompatActivity {

    public static String result="";
    public static final int CAMERA_REQ_CODE = 111;
    public static final int DECODE = 1;
    public static final int GENERATE = 2;
    private static final int REQUEST_CODE_SCAN_ONE = 0X01;
    private static final int REQUEST_CODE_SCAN = 0X01;
    public static final String RESULT = "SCAN_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }
    public void loadScanKitBtnClick(View view) {
        requestPermission(CAMERA_REQ_CODE, DECODE);
    }
    /**
     * 申请权限
     */
    private void requestPermission(int requestCode, int mode) {
        if (mode == DECODE) {
            decodePermission(requestCode);
        }
        else if (mode == GENERATE) {
            generatePermission(requestCode);
        }
    }
    /**
     * 申请权限
     */
    private void decodePermission(int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
    }
    private void generatePermission(int requestCode) {
        System.out.println("3");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                requestCode);
    }
    /**
     * Call back the permission application result. If the permission application is successful, the barcode scanning view will be displayed.
     回调权限应用程序结果。如果权限申请成功，将显示条形码扫描视图。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //默认扫码格式
//        if (requestCode == CAMERA_REQ_CODE) {
//            ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().create());
//        }
        if (permissions == null || grantResults == null) {
            return;
        }
        if (grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (requestCode == CAMERA_REQ_CODE) {
            ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create());
        }
    }
    /**
     *扫码结果的回调
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView saoma_text = findViewById(R.id.saoma_text);
        //当扫码页面结束后，处理扫码结果
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        //从onActivityResult返回data中，用 ScanUtil.RESULT作为key值取到HmsScan返回值
        if (requestCode == REQUEST_CODE_SCAN) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    result+=((HmsScan) obj).getOriginalValue();
                    saoma_text.setText(result);
                    Toast.makeText(this, ((HmsScan) obj).getOriginalValue(), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}