package com.qr.core.zxing;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.qr.core.R;
import com.qr.core.zxing.scaner.camera.CameraManager;
import com.qr.core.zxing.scaner.decode.DecodeImage;
import com.qr.core.zxing.scaner.decode.PlanarYUVLuminanceSource;
import com.qr.core.zxing.scaner.decoding.InactivityTimer;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author vondear
 */
public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = CaptureActivity.class.getName();
    public static final String SCAN_RESULT = CaptureActivity.class.getName() + "_DATA";
    public static final int GET_IMAGE_FROM_PHONE = 5002;

    private CameraManager cameraManager;
    private Disposable autoFocusDisposable;
    private Disposable previewDisposable;

    private InactivityTimer inactivityTimer;

    private RelativeLayout mContainer = null;
    private RelativeLayout mCropLayout = null;
    private boolean hasSurface;
    private boolean mFlashing = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout._activity_capture);

        //检查权限
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "无相机控制权限!!!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        //界面控件初始化
        initDecode();
        initView();
        //扫描动画初始化
        initScannerAnimation();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            //Camera初始化
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (!hasSurface) {
                        hasSurface = true;
                        initCamera(holder);
                    }
                }
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    hasSurface = false;
                }
            });
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        autoFocusDisposable.dispose();
        autoFocusDisposable = null;
        previewDisposable.dispose();
        previewDisposable = null;
        cameraManager.stopPreview();
        cameraManager.closeDriver();
        cameraManager = null;
    }
    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                // 开始对图像资源解码
                Result rawResult = DecodeImage.decodeFromPhoto(photo);
                if (rawResult != null) {
                    Intent intent = new Intent();
                    intent.putExtra(SCAN_RESULT,rawResult.getText());
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(this, "解析图片失败!!!,请选择一个正确的二维码!!!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.top_mask) {
            light();
        } else if (viewId == R.id.top_back) {
            onBackPressed();
        } else if (viewId == R.id.top_openpicture) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "无读写外部存储权限!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GET_IMAGE_FROM_PHONE);
        }
    }
    private void initView() {
        mContainer = findViewById(R.id.capture_containter);
        mCropLayout = findViewById(R.id.capture_crop_layout);

        findViewById(R.id.top_openpicture).setOnClickListener(this);
        findViewById(R.id.top_back).setOnClickListener(this);
        findViewById(R.id.top_mask).setOnClickListener(this);
    }
    private void initDecode() {
        multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<>();
        Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<>(5);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
        // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
        Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<>(PRODUCT_FORMATS.size() + 4);
        ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
        ONE_D_FORMATS.add(BarcodeFormat.ITF);
        Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<>(1);
        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
        Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<>(1);
        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

        // 这里设置可扫描的类型，我这里选择了都支持
        decodeFormats.addAll(ONE_D_FORMATS);
        decodeFormats.addAll(QR_CODE_FORMATS);
        decodeFormats.addAll(DATA_MATRIX_FORMATS);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        multiFormatReader.setHints(hints);
    }
    private void initScannerAnimation() {
        ImageView mQrLineView = findViewById(R.id.capture_scan_line);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mQrLineView.startAnimation(animation);
    }
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            cameraManager = new CameraManager(this);
            cameraManager.openDriver(surfaceHolder);
            Point point = cameraManager.getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = mCropLayout.getWidth() * width.get() / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height.get() / mContainer.getHeight();
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException | RuntimeException ioe) {
            Toast.makeText(this, "打开相机失败!!!", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        // 开始预览
        cameraManager.startPreview();
        autoFocusDisposable = Observable.interval(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        cameraManager.requestAutoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                Log.d(TAG, "AutoFocus: " + success);
                            }
                        });
                    }
                });

        previewDisposable = Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(final ObservableEmitter<byte[]> emitter) throws Exception {
                cameraManager.requestPreviewFrame(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        emitter.onNext(data);
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .map(new Function<byte[], Result>() {
                    @Override
                    public Result apply(byte[] bytes) throws Exception {
                        return decode(bytes, cameraManager.getCameraResolution().x, cameraManager.getCameraResolution().y);
                    }
                })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                return Observable.timer(200, TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                })
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        handleDecode(result);
                    }
                });
    }
    public void setCropWidth(int cropWidth) {
        CameraManager.FRAME_WIDTH = cropWidth;
    }
    public void setCropHeight(int cropHeight) {
        CameraManager.FRAME_HEIGHT = cropHeight;
    }
    private void light() {
        if (mFlashing) {
            mFlashing = false;
            // 开闪光灯
            cameraManager.openLight();
        } else {
            mFlashing = true;
            // 关闪光灯
            cameraManager.offLight();
        }
    }
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        Intent intent = new Intent();
        intent.putExtra(SCAN_RESULT,result.getText());
        setResult(RESULT_OK,intent);
        finish();
    }
    private MultiFormatReader multiFormatReader;
    private Result decode(byte[] data, int width, int height) {
        Result rawResult = null;

        //modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        // Here we are swapping, that's the difference to #11
        int tmp = width;
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = cameraManager.buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException e) {
            // continue
        } finally {
            multiFormatReader.reset();
        }

        return rawResult;
    }
}