package cn.xinyi.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import cn.xinyi.plugin.model.PersonModel;
import cn.xinyi.plugin.model.PlateModel;

/**
 * Created by zhiren.zhang on 2018/11/23.
 */

public class PluginBroadcastReceiver extends BroadcastReceiver {
    static PluginBroadcastReceiver receiver;

    private PluginBroadcastReceiver() {
    }

    public static PluginBroadcastReceiver getInstance() {
        if (receiver == null) {
            receiver = new PluginBroadcastReceiver();
        }
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // result==[, 张志仁, 男, 汉, 1989-12-26, 广东省深圳市南山区沙河西路4089号深职院09届机电学院, 360281198912261455,
        //  result==[京FA7890, 蓝, 1, 1, 89, 158, 0, 88, 172, 558, 282, 210, 0, 8, null]  pic_path==/storage/emulated/0/Pictures/1542955763983.jpg  orc_type==-1

        int orc_type = intent.getIntExtra("orc_type", -100);
        String pic_path = intent.getStringExtra("pic_path");
        String result = intent.getStringExtra("result");
        String[] split = result.replace("[", "").replace("]", "").split(", ");
        switch (orc_type) {
            case OcrUtils.ORC_TYPE_PLATE:
                PlateModel plateModel = new PlateModel();
                plateModel.setPlateNumber(split[0]);
                plateModel.setColor(split[1]);
                plateModel.setScore(split[4]);
                if (callBack != null) {
                    callBack.callBackPlate(plateModel);
                }
                break;
            case OcrUtils.ORC_TYPE_SFZ:
                PersonModel personModel = new PersonModel();
                personModel.setName(split[1])
                        .setSex(split[2])
                        .setNation(split[3])
                        .setBirthday(split[4])
                        .setAddress(split[5])
                        .setIdcard(split[6]);
                if (callBack != null) {
                    callBack.callBackSfz(personModel);
                }
                break;
        }

    }


    DefaultCallBack callBack;

    public void registerReceiver(Activity activity, DefaultCallBack callBack) {
        this.callBack = callBack;
        IntentFilter intentFilter = new IntentFilter("cn.xinyi.plugin.orc.broadcast");
        activity.registerReceiver(this, intentFilter);


    }

    public void unregisterReceiver(Activity activity) {
        activity.unregisterReceiver(this);
    }


}
