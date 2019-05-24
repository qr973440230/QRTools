package com.qr.rx;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.qr.core.utils.UriUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RxCamera extends Fragment {
    public static final String TAG = RxCamera.class.getName();
    public static final String ERROR_NO_CAMERA_PERMISSION = "No Camera Permission";
    public static final String ERROR_NO_WRITE_EXTERNAL_STORAGE_PERMISSION = "No Write_External_Storage Permission";
    public static final String ERROR_CANCEL = "Cancel";
    public static final String ERROR_NO_CAMERA_APPLICATION = "No Camera Application";

    private final List<CameraResultCallback> callbacks;
    public RxCamera(){
        callbacks = new LinkedList<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        for (CameraResultCallback callback : callbacks) {
            callback.onActivityResult(requestCode,resultCode,data);
        }
    }

    // Camera相关
    public Single<Uri> getSystemCameraTakePictureSingle(){
        // 检查权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                return Single.error(new Throwable(ERROR_NO_CAMERA_PERMISSION));
            }
            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                return Single.error(new Throwable(ERROR_NO_WRITE_EXTERNAL_STORAGE_PERMISSION));
            }
        }

        return Single.<Uri>create(emitter -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
                final Uri uri = UriUtils.createPicturesUri(getContext());
                final int code = uri.hashCode() & 0x0000ffff;
                final CameraResultCallback callback = (requestCode, resultCode, data) -> {
                    if(requestCode == code && !emitter.isDisposed()){
                        if(resultCode == Activity.RESULT_OK){
                            emitter.onSuccess(uri);
                        }else{
                            emitter.onError(new Throwable(ERROR_CANCEL));
                        }
                    }
                };

                callbacks.add(callback);
                emitter.setDisposable(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        callbacks.remove(callback);
                    }
                });

                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(intent,code);
            }else{
                emitter.onError(new Throwable(ERROR_NO_CAMERA_APPLICATION));
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    // Video相关
    public Single<Uri> getSystemCameraRecordVideoSingle(){
        // 检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                return Single.error(new Throwable(ERROR_NO_CAMERA_PERMISSION));
            }
            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                return Single.error(new Throwable(ERROR_NO_WRITE_EXTERNAL_STORAGE_PERMISSION));
            }
        }

        return Single.<Uri>create(emitter -> {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if(intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
                final Uri uri = UriUtils.createMoviesUri(getContext());
                final int code = uri.hashCode() & 0x0000ffff;
                final CameraResultCallback callback = (requestCode, resultCode, data) -> {
                    if(requestCode == code && !emitter.isDisposed()){
                        if(resultCode == Activity.RESULT_OK){
                            emitter.onSuccess(uri);
                        }else{
                            emitter.onError(new Throwable(ERROR_CANCEL));
                        }
                    }
                };

                callbacks.add(callback);
                emitter.setDisposable(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        callbacks.remove(callback);
                    }
                });

                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(intent,code);
            }else{
                emitter.onError(new Throwable(ERROR_NO_CAMERA_APPLICATION));
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public static RxCamera attach(FragmentActivity activity){
        RxCamera imagePicker = (RxCamera)activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if(imagePicker == null){
            imagePicker = new RxCamera();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(imagePicker,TAG)
                    .commitNow();
        }
        return imagePicker;
    }

    private interface CameraResultCallback{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
