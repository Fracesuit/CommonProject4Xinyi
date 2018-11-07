package cn.net.xinyi.xmjt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.camera.internal.utils.SDCardUtils2;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.permission.DefaultRequestPermissionsListener;
import com.xinyi_tech.comm.permission.PermissionsHelp;

import cn.xinyi.orc.OcrUtils;


public class MainActivity extends AppCompatActivity {
    TextView tv_orc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ArrowView arrowView=new ArrowView()
        tv_orc = findViewById(R.id.tv_orc);
        tv_orc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsHelp.with(MainActivity.this)
                        .requestPermissions(new DefaultRequestPermissionsListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void grant() {
                                OcrUtils.getInstance(MainActivity.this).startPlateOrc(100);
                            }
                        }, PermissionsHelp.WRITE_EXTERNAL_STORAGE, PermissionsHelp.CAMERA, Manifest.permission.READ_PHONE_STATE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            String result = data.getStringExtra("result");
            tv_orc.setText(result);
            Log.e("result", "result==" + result);
        }
    }
}
