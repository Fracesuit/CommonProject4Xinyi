package cn.xinyi.orc;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by zhiren.zhang on 2018/11/7.
 */

public class PathUtils {

    ///data/data/0/com.it.suitapp/cache
    public static File getInnerCache(Context context) {
        return context.getCacheDir();
    }


    private static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }



    ///storage/emulated/0/Android/data/com.it.suitapp/cache
    public static File getExternalPrivateCache(Context context) {
        return isSDCardEnable() ? context.getExternalCacheDir() : getInnerCache(context);
    }
}
