package cn.net.xinyi.xmjt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qihoo360.replugin.RePlugin;
import com.xinyi_tech.comm.permission.DefaultRequestPermissionsListener;
import com.xinyi_tech.comm.permission.PermissionsHelp;
import com.xinyi_tech.comm.util.ToastyUtil;

import cn.xinyi.orc.OcrUtils;


public class MainActivity1 extends AppCompatActivity {


    TextView tv_orc;
    TextView tv_idcard;
    TextView tv_orc_plate_uninstall;
    TextView tv_orc_idcard_uninstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ArrowView arrowView=new ArrowView()
        tv_orc = findViewById(R.id.tv_orc);
        tv_idcard = findViewById(R.id.tv_idcard);
        tv_orc_plate_uninstall = findViewById(R.id.tv_orc_plate_uninstall);
        tv_orc_idcard_uninstall = findViewById(R.id.tv_orc_idcard_uninstall);
        tv_orc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsHelp.with(MainActivity1.this)
                        .requestPermissions(new DefaultRequestPermissionsListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void grant() {
                                OcrUtils.getInstance(MainActivity1.this).startPlateOrc(300);
                            }
                        }, PermissionsHelp.WRITE_EXTERNAL_STORAGE, PermissionsHelp.CAMERA, Manifest.permission.READ_PHONE_STATE);
            }
        });
        tv_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsHelp.with(MainActivity1.this)
                        .requestPermissions(new DefaultRequestPermissionsListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void grant() {
                                OcrUtils.getInstance(MainActivity1.this).startIdcardOrc(400);
                            }
                        }, PermissionsHelp.WRITE_EXTERNAL_STORAGE, PermissionsHelp.CAMERA, Manifest.permission.READ_PHONE_STATE);
            }
        });
        tv_orc_plate_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean uninstall = RePlugin.uninstall(OcrUtils.PLUGIN_NAME_PLATE);
                ToastyUtil.warningShort("车牌卸载："+uninstall);
            }
        });
        tv_orc_idcard_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean uninstall = RePlugin.uninstall(OcrUtils.PLUGIN_NAME_IDCARD);
                ToastyUtil.warningShort("证件卸载："+uninstall);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && data != null) {
            String result = data.getStringExtra("result");
            tv_orc.setText(result);
            Log.e("result", "result==" + result);
        }
        else  if (requestCode == 400 && data != null) {
            String result = data.getStringExtra("result");
            tv_idcard.setText(result);
            Log.e("result", "result==" + result);
        }
    }
}
