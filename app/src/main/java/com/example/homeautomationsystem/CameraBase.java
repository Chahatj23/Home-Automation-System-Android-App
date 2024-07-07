//package com.example.homeautomationsystem;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.Size;
//import android.view.Surface;
//import android.view.TextureView;
//
//public abstract class CameraBase {
//
//    public enum CameraType {
//        FRONT_FACING,
//        BACK_FACING
//    }
//
//    private Context context;
//    private TextureView textureView;
//    private CameraType cameraType;
//    private CameraCapture cameraCapture;
//
//    public CameraBase(Context context, TextureView textureView, CameraType cameraType) {
//        this.context = context;
//        this.textureView = textureView;
//        this.cameraType = cameraType;
//        this.cameraCapture = new CameraCapture(context, textureView, cameraType);
//    }
//
//    public void startCamera() {
//        cameraCapture.startCamera();
//    }
//
//    public void releaseCamera() {
//        cameraCapture.releaseCamera();
//    }
//
//    public void takePicture(CameraCapture.OnImageCapturedListener listener) {
//        cameraCapture.takePicture(listener);
//    }
//
//    protected abstract Size getDesiredPreviewSize();
//}
