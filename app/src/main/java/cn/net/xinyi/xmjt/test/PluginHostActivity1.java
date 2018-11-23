package cn.net.xinyi.xmjt.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ImageUtils;
import com.qihoo360.replugin.RePlugin;
import com.xinyi_tech.comm.util.ToastyUtil;

import cn.net.xinyi.xmjt.R;
import cn.xinyi.plugin.DefaultCallBack;
import cn.xinyi.plugin.OcrUtils;
import cn.xinyi.plugin.model.PersonModel;
import cn.xinyi.plugin.model.PlateModel;


public class PluginHostActivity1 extends AppCompatActivity {
    TextView tv_orc;
    TextView tv_idcard;
    TextView tv_test_plugin;
    TextView tv_orc_plate_uninstall;
    TextView tv_orc_idcard_uninstall;
    TextView tv_test_uninstall;
    TextView tv_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ArrowView arrowView=new ArrowView()
        tv_orc = findViewById(R.id.tv_orc);
        tv_idcard = findViewById(R.id.tv_idcard);
        tv_test_plugin = findViewById(R.id.tv_test_plugin);
        tv_orc_plate_uninstall = findViewById(R.id.tv_orc_plate_uninstall);
        tv_orc_idcard_uninstall = findViewById(R.id.tv_orc_idcard_uninstall);
        tv_test_uninstall = findViewById(R.id.tv_test_uninstall);
        tv_go = findViewById(R.id.tv_go);
        tv_orc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                OcrUtils.getInstance(PluginHostActivity1.this, new DefaultCallBack() {
                    @Override
                    public void callBackPlate(PlateModel model) {
                        ToastyUtil.successShort(model.toString());
                    }
                }).startPlateOrc();
            }
        });
        tv_idcard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                OcrUtils.getInstance(PluginHostActivity1.this, new DefaultCallBack() {
                    @Override
                    public void callBackSfz(PersonModel model) {
                        ToastyUtil.successShort(model.toString());
                    }
                }).startIdcardOrc(200);
            }
        });

        tv_orc_plate_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean uninstall = RePlugin.uninstall(OcrUtils.PLUGIN_NAME_PLATE);
                Toast.makeText(PluginHostActivity1.this, "车牌卸载：" + uninstall, Toast.LENGTH_SHORT).show();
            }
        });

        tv_orc_idcard_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean uninstall = RePlugin.uninstall(OcrUtils.PLUGIN_NAME_IDCARD);
                Toast.makeText(PluginHostActivity1.this, "证件卸载：" + uninstall, Toast.LENGTH_SHORT).show();
            }
        });

        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PluginHostActivity1.this, PluginHostActivity1.class);
                PluginHostActivity1.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        OcrUtils.releaseOrc(this);
        super.onDestroy();
    }
}
