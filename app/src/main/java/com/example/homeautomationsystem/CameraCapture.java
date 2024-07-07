//package com.example.homeautomationsystem;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.media.Image;
//import android.media.ImageReader;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.util.Size;
//import android.view.Surface;
//import android.view.TextureView;
//
//import androidx.annotation.NonNull;
//
//import java.nio.ByteBuffer;
//
//public class CameraCapture {
//
//    public interface OnImageCapturedListener {
//        void onImageCaptured(Bitmap image);
//    }
//
//    private Context context;
//    private TextureView textureView;
//    private CameraBase.CameraType cameraType;
//    private Size previewSize;
//    private ImageReader imageReader;
//    private Camera2Manager camera2Manager;
//    private HandlerThread backgroundThread;
//    private Handler backgroundHandler;
//
//    public CameraCapture(Context context, TextureView textureView, CameraBase.CameraType cameraType) {
//        this.context = context;
//        this.textureView = textureView;
//        this.cameraType = cameraType;
//        this.camera2Manager = new Camera2Manager(context, cameraType);
//    }
//
//    public void startCamera() {
//        camera2Manager.setCameraSurfaceProvider(() -> {
//            Surface surface = getTextureView().getSurfaceTexture().getSurface();
//            return new Surface[]{surface};
//        });
//
//        camera2Manager.setCameraStateListener(new Camera2Manager.CameraStateListener() {
//            @Override
//            public void onCameraConfigured(Size previewSize) {
//                setPreviewSize(previewSize);
//            }
//
//            @Override
//            public void onPreviewFrame(Image image) {
//                // Process preview frame (optional)
//            }
//        });
//
//        camera2Manager.openCamera();
//    }
//
//    public void releaseCamera() {
//        camera2Manager.closeCamera();
//    }
//
//    public void takePicture(OnImageCapturedListener listener) {
//        if (camera2Manager != null) {
//            camera2Manager.takePicture(image -> {
//                Bitmap bitmap = convertImageToBitmap(image);
//                listener.onImageCaptured(bitmap);
//                image.close();
//            });
//        }
//    }
//
//    private Bitmap convertImageToBitmap(Image image) {
//        Image.Plane[] planes = image.getPlanes();
//        ByteBuffer buffer = planes[0].getBuffer();
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//        int pixelStride = planes[0].getPixelStride();
//        int rowStride = planes[0].getRowStride();
//        int rowPadding = rowStride - pixelStride * width;
//
//        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
//        bitmap.copyPixelsFromBuffer(buffer);
//
//        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
//    }
//
//    private TextureView getTextureView() {
//        return textureView;
//    }
//
//    private void setPreviewSize(Size previewSize) {
//        this.previewSize = previewSize;
//    }
//}
