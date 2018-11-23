package cn.xinyi.plugin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresPermission;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhiren.zhang on 2018/11/7.
 */

public class OcrUtils {

   /* private String[][] type2 = {{"机读码", "3000"}, {"护照", "13"},
            {"居民身份证", "2"}, {"港澳通行证", "9"}, {"大陆居民往来台湾通行证", "11"},
            {"签证", "12"}, {"新版港澳通行证", "22"}, {"中国驾照", "5"},
            {"中国行驶证", "6"}, {"香港身份证", "1001"}, {"回乡证(正面)", "14"},
            {"回乡证(背面)", "15"}, {"澳门身份证", "1005"}, {"新版澳门身份证", "1012"},
            {"台胞证", "10"}, {"新版台胞证(正面)", "25"}, {"新版台胞证(背面)", "26"},
            {"台湾身份证(正面)", "1031"}, {"台湾身份证(背面)", "1032"},{"中国军官证", "7"},
            {"全民健康保险卡", "1030"}, {"马来西亚身份证", "2001"}, {"新加坡身份证", "2004"},
            {"新西兰驾照", "2003"}, {"加利福尼亚驾照", "2002"}, {"印度尼西亚身份证", "2010"}, {"泰国身份证", "2011"}};*/


    public static final int ORC_TYPE_PLATE = -1;//车牌
    public static final int ORC_TYPE_SFZ = 2;//居民身份证

    private static final int NET_START = 1;
    private static final int NET_ERROR = 2;
    private static final int NET_PROGRESS = 4;


    private static final int PLUGIN_START = 100;
    private static final int PLUGIN_ERROR = 101;
    private static final int PLUGIN_END = 102;//启动插件


    private static OcrUtils ocrUtils;
    ProgressDialog dialog;
    Activity activity;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NET_START:
                    dialog.setMessage("正在下载插件,请稍等...");
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    break;
                case NET_ERROR:
                    dialog.setMessage("下载出现错误,请检查网络");
                    dialog.setCancelable(true);
                    break;
                case NET_PROGRESS:
                    int progress = (int) msg.obj;
                    dialog.setMessage("正在下载中," + progress + "%");
                    break;
                case PLUGIN_START:
                    dialog.setMessage("插件下载完成,正在安装插件");
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    break;
                case PLUGIN_END:
                    dialog.dismiss();
                    String[] split = ((String) msg.obj).split(",");

                    int cameras = Camera.getNumberOfCameras();
                    if (cameras > 0) {
                        Intent intent = RePlugin.createIntent(split[0], split[1]);
                        intent.putExtra("ocrType", msg.arg1);
                        RePlugin.startActivity(activity, intent);
                    } else {
                        Toast.makeText(activity, "没有摄像机设备", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case PLUGIN_ERROR:
                    dialog.dismiss();
                    Toast.makeText(activity, "插件安装失败", Toast.LENGTH_SHORT).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private static String ORC_BASE_URL = "http://10.24.4.100:8080/ocr/%s.apk";
    private static String ORC_BASE_PATH;

    public static final String PLUGIN_NAME_PLATE = "plate";
    public static final String PLUGIN_NAME_IDCARD = "idcard";

    private OcrUtils(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
        dialog.setTitle("初始化插件");
        dialog.setMessage("请稍等...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        ORC_BASE_PATH = PathUtils.getExternalPrivateCache(activity).getPath() + "/%s.apk";


    }

    public static OcrUtils getInstance(Activity activity, DefaultCallBack callBack) {
        if (ocrUtils == null) {
            ocrUtils = new OcrUtils(activity);
        }
        PluginBroadcastReceiver.getInstance().registerReceiver(activity, callBack);
        return ocrUtils;
    }

    // 务必要调用这个释放广播
    public static void releaseOrc(Activity activity) {
        PluginBroadcastReceiver.getInstance().unregisterReceiver(activity);
    }


    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    public void startIdcardOrc(int ocrType) {
        startOrc(PLUGIN_NAME_IDCARD, "cn.xinyi.orc.IdcardOcrCameraActivity", ocrType);

    }

    //这三个权限是必须要的
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    public void startPlateOrc() {
        startOrc(PLUGIN_NAME_PLATE, "cn.xinyi.orc.PlateOcrCameraActivity", -1);
    }


    private void startOrc(final String pluginName, final String className, final int ocrType) {
        if (RePlugin.isPluginInstalled(pluginName)) {
            Message message = new Message();
            message.what = PLUGIN_END;
            message.obj = pluginName + "," + className;
            message.arg1 = ocrType;
            handler.sendMessage(message);

        } else {
            final String path = String.format(ORC_BASE_PATH, pluginName);
            File file = new File(path);
            if (file.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        installPluin(pluginName, className, ocrType);
                    }
                }).start();

            } else {
                downloadAndInstall(pluginName, className, ocrType);
            }

        }
    }

    private void downloadAndInstall(final String pluginName, final String className, final int ocrType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = NET_START;
                handler.sendMessage(message);
                File download = download(pluginName);
                if (download == null) {
                    message = new Message();
                    message.what = NET_ERROR;
                    handler.sendMessage(message);
                } else {
                    installPluin(pluginName, className, ocrType);
                }

            }
        }).start();
    }

    private int retryTimes = 3;

    private void installPluin(String pluginName, String className, int ocrType) {
        String path = String.format(ORC_BASE_PATH, pluginName);
        Message message = new Message();
        message.what = PLUGIN_START;
        handler.sendMessage(message);
        PluginInfo install = RePlugin.install(path);
        if (install != null) {
            boolean preload = RePlugin.preload(install);
            message = new Message();
            message.what = PLUGIN_END;
            message.obj = pluginName + "," + className;
            message.arg1 = ocrType;
            handler.sendMessage(message);
        } else {
            if (retryTimes-- > 0) {
                //重试3次
                downloadAndInstall(pluginName, className, ocrType);
            } else {
                message = new Message();
                message.what = PLUGIN_ERROR;
                handler.sendMessage(message);
            }

        }
    }


    private File download(String pluginName) {
        String urlPath = String.format(ORC_BASE_URL, pluginName);
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1, filePathUrl.lastIndexOf("."));

            System.out.println("file length---->" + fileLength);


            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

            String path = String.format(ORC_BASE_PATH, fileName);
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];

            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                Message message = new Message();
                message.what = NET_PROGRESS;
                message.obj = len * 100 / fileLength;
                handler.sendMessage(message);
            }
            bin.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return file;
        }
    }
}
