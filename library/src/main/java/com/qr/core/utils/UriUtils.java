package com.qr.core.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.qr.core.camera.CameraFileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UriUtils {
    // 创建MUSICS目录下的Uri
    public static Uri createMusicsUri(final Context context){
        return createMusicsUri(context,
                new SimpleDateFormat("yyyyMMdd-HHmmss",
                        Locale.getDefault()).format(new Date()) + ".mp3");
    }
    // 创建MUSICS目录下的Uri
    public static Uri createMusicsUri(final Context context, @NonNull final String fileName){
        return createUri(context,
                Environment.getExternalStorageDirectory()
                        .getAbsolutePath() +
                        File.separator +
                        Environment.DIRECTORY_MUSIC,
                fileName);
    }

    // 创建PICTURES目录下的Uri
    public static Uri createPicturesUri(final Context context){
        return createPicturesUri(context,
                new SimpleDateFormat("yyyyMMdd-HHmmss",
                        Locale.getDefault()).format(new Date()) + ".jpg");
    }
    // 创建PICTURES目录下的Uri
    public static Uri createPicturesUri(final Context context,@NonNull final String fileName){
        return createUri(context,
                Environment.getExternalStorageDirectory()
                        .getAbsolutePath() +
                        File.separator +
                        Environment.DIRECTORY_PICTURES,
                fileName);
    }

    // 创建MOVIES目录下的Uri
    public static Uri createMoviesUri(final Context context) {
        return createMoviesUri(context,
                new SimpleDateFormat("yyyyMMdd-HHmmss",
                        Locale.getDefault()).format(new Date()) + ".mp4");
    }
    // 创建MOVIES目录下的Uri
    public static Uri createMoviesUri(final Context context,@NonNull final String fileName){
        return createUri(context,
                Environment.getExternalStorageDirectory()
                        .getAbsolutePath() +
                        File.separator +
                        Environment.DIRECTORY_MOVIES,
                fileName);
    }
    public static Uri createUri(final Context context,@NonNull final String path,@NonNull final String fileName){
        return fileToUri(context,new File(path,fileName));
    }

    public static Uri fileToUri(final Context context,final File file){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return FileProvider.getUriForFile(context, CameraFileProvider.class.getName(),file);
        }else{
            return Uri.fromFile(file);
        }
    }
}
