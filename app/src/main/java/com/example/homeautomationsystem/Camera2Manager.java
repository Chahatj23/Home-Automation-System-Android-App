//package com.example.homeautomationsystem;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.ImageFormat;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraCaptureSession;
//import android.hardware.camera2.CameraCharacteristics;
//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.hardware.camera2.CaptureRequest;
//import android.hardware.camera2.params.StreamConfigurationMap;
//import android.media.Image;
//import android.media.ImageReader;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.util.Log;
//import android.util.Size;
//import android.view.Surface;
//import android.view.TextureView;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//
//import java.util.Arrays;
//
//public class Camera2Manager {
//
//    private static final String TAG = "Camera2Manager";
//    private static final int MAX_IMAGES = 1; // Maximum number of images in the image reader queue
//
//    private Context context;
//    private TextureView textureView;
//    private CameraDevice cameraDevice;
//    private CameraCaptureSession captureSession;
//    private ImageReader imageReader;
//    private CameraStateListener cameraStateListener;
//    private CameraBase.CameraType cameraType;
//
//    public interface CameraStateListener {
//        void onCameraConfigured(Size previewSize);
//
//        void onPreviewFrame(Image image);
//    }
//
//    public Camera2Manager(Context context, CameraBase.CameraType cameraType) {
//        this.context = context;
//        this.cameraType = cameraType;
//    }
//
//    public void setCameraSurfaceProvider(TextureView.SurfaceTextureListener surfaceTextureListener) {
//        textureView.setSurfaceTextureListener(surfaceTextureListener);
//    }
//
//    public void setCameraStateListener(CameraStateListener listener) {
//        this.cameraStateListener = listener;
//    }
//
//    public void openCamera() {
//        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String cameraId = getCameraId(cameraManager);
//            if (cameraId != null) {
//                StreamConfigurationMap map = cameraManager.getCameraCharacteristics(cameraId)
//                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                if (map != null) {
//                    Size[] outputSizes = map.getOutputSizes(textureView.getClass());
//
//                    // Choose a suitable preview size (e.g., the largest available size)
//                    Size previewSize = outputSizes[0]; // Example: Select the first available size
//                    configureImageReader(previewSize);
//
//                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
//                        @Override
//                        public void onOpened(@NonNull CameraDevice camera) {
//                            cameraDevice = camera;
//                            createCaptureSession();
//                        }
//
//                        @Override
//                        public void onDisconnected(@NonNull CameraDevice camera) {
//                            cameraDevice.close();
//                            cameraDevice = null;
//                        }
//
//                        @Override
//                        public void onError(@NonNull CameraDevice camera, int error) {
//                            cameraDevice.close();
//                            cameraDevice = null;
//                            Log.e(TAG, "Camera2 error: " + error);
//                        }
//                    }, null);
//                }
//            }
//        } catch (CameraAccessException e) {
//            Log.e(TAG, "Camera access exception: " + e.getMessage());
//        }
//    }
//
//    public void closeCamera() {
//        if (captureSession != null) {
//            captureSession.close();
//            captureSession = null;
//        }
//        if (cameraDevice != null) {
//            cameraDevice.close();
//            cameraDevice = null;
//        }
//        if (imageReader != null) {
//            imageReader.close();
//            imageReader = null;
//        }
//    }
//
//    private String getCameraId(CameraManager cameraManager) throws CameraAccessException {
//        String[] cameraIds = cameraManager.getCameraIdList();
//        for (String id : cameraIds) {
//            if (cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.LENS_FACING)
//                    == cameraType.ordinal()) {
//                return id;
//            }
//        }
//        return null;
//    }
//
//    private void configureImageReader(Size previewSize) {
//        imageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(),
//                ImageFormat.JPEG, MAX_IMAGES);
//        imageReader.setOnImageAvailableListener(reader -> {
//            Image image = reader.acquireLatestImage();
//            if (image != null) {
//                cameraStateListener.onPreviewFrame(image);
//                image.close();
//            }
//        }, null);
//    }
//
//    private void createCaptureSession() {
//        try {
//            Surface surface = new Surface(textureView.getSurfaceTexture());
//            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()),
//                    new CameraCaptureSession.StateCallback() {
//                        @Override
//                        public void onConfigured(@NonNull CameraCaptureSession session) {
//                            captureSession = session;
//                            startPreview();
//                        }
//
//                        @Override
//                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
//                            Log.e(TAG, "Failed to configure camera capture session");
//                        }
//                    }, null);
//        } catch (CameraAccessException e) {
//            Log.e(TAG, "Camera access exception: " + e.getMessage());
//        }
//    }
//
//    private void startPreview() {
//        try {
//            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            builder.addTarget(new Surface(textureView.getSurfaceTexture()));
//            builder.addTarget(imageReader.getSurface());
//
//            cameraDevice.createCaptureSession(Arrays.asList(new Surface(textureView.getSurfaceTexture()),
//                    imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession session) {
//                    try {
//                        session.setRepeatingRequest(builder.build(), null, backgroundHandler);
//                        cameraStateListener.onCameraConfigured(previewSize);
//                    } catch (CameraAccessException e) {
//                        Log.e(TAG, "Camera access exception: " + e.getMessage());
//                    }
//                }
//
//                @Override
//                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
//                    Log.e(TAG, "Failed to configure camera capture session");
//                }
//            }, null);
//        } catch (CameraAccessException e) {
//            Log.e(TAG, "Camera access exception: " + e.getMessage());
//        }
//    }
//
//    public void takePicture(ImageReader.OnImageAvailableListener listener) {
//        if (captureSession != null) {
//            try {
//                CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//                captureBuilder.addTarget(imageReader.getSurface());
//
//                captureSession.capture(captureBuilder.build(), null, backgroundHandler);
//            } catch (CameraAccessException e) {
//                Log.e(TAG, "Camera access exception: " + e.getMessage());
//            }
//        }
//    }
//}
