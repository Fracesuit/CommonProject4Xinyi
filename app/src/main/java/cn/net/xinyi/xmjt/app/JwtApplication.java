package cn.net.xinyi.xmjt.app;

import android.content.Context;
import android.content.res.Configuration;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginConfig;
import com.xinyi_tech.comm.BaseApplication;
import com.xinyi_tech.comm.net.retrofit2.config.RetrofitManager;

import cn.net.xinyi.xmjt.BuildConfig;

/**
 * Created by zhiren.zhang on 2018/10/18.
 */

public class JwtApplication extends BaseApplication {

    private static ApiService apiService;

    public JwtApplication() {
        super(BuildConfig.ISDEBUG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RePlugin.App.onCreate();
        //网络
        apiService = createApiService(RetrofitManager.newBuilder(BuildConfig.HOST, String.valueOf(BuildConfig.PORT)), ApiService.class);

        //RePlugin.preload("ocr");
    }

    //获取网络引擎
    public static ApiService getApiService() {
        return apiService;
    }




    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        RePluginConfig c = new RePluginConfig();
        c.setVerifySign(!BuildConfig.DEBUG);
        RePlugin.App.attachBaseContext(this, c);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onConfigurationChanged(config);
    }
}
