package io.github.memfis19.annca.internal.controller.impl;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;

import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;
import io.github.memfis19.annca.internal.configuration.ConfigurationProvider;
import io.github.memfis19.annca.internal.controller.view.CameraView;
import io.github.memfis19.annca.internal.manager.CameraManager;
import io.github.memfis19.annca.internal.manager.impl.Camera1Manager;
import io.github.memfis19.annca.internal.manager.impl.ParametersHandler;
import io.github.memfis19.annca.internal.manager.listener.CameraCloseListener;
import io.github.memfis19.annca.internal.manager.listener.CameraOpenListener;
import io.github.memfis19.annca.internal.manager.listener.CameraPhotoListener;
import io.github.memfis19.annca.internal.manager.listener.CameraVideoListener;
import io.github.memfis19.annca.internal.ui.view.AutoFitSurfaceView;
import io.github.memfis19.annca.internal.utils.CameraHelper;
import io.github.memfis19.annca.internal.utils.Size;

/**
 * Created by memfis on 7/7/16.
 */

@SuppressWarnings("deprecation")
public class Camera1Controller implements io.github.memfis19.annca.internal.controller.CameraController<Integer>,
        CameraOpenListener<Integer, SurfaceHolder.Callback>, CameraPhotoListener, CameraCloseListener<Integer>, CameraVideoListener {

    private final static String TAG = "Camera1Controller";

    private Integer currentCameraId;
    private ConfigurationProvider configurationProvider;
    private CameraManager<Integer, SurfaceHolder.Callback, Camera.Parameters, Camera> cameraManager;
    private CameraView cameraView;

    private File outputFile;

    public Camera1Controller(CameraView cameraView, ConfigurationProvider configurationProvider) {
        this.cameraView = cameraView;
        this.configurationProvider = configurationProvider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        cameraManager = Camera1Manager.getInstance();
        cameraManager.initializeCameraManager(configurationProvider, cameraView.getActivity());
        currentCameraId = cameraManager.getFaceBackCameraId();
    }

    @Override
    public void onResume() {
        cameraManager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPause() {
        cameraManager.closeCamera(null);
    }

    @Override
    public void onDestroy() {
        cameraManager.releaseCameraManager();
    }

    @Override
    public void takePhoto() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), AnncaConfiguration.MEDIA_ACTION_PHOTO);
        cameraManager.takePhoto(outputFile, this);
    }

    @Override
    public void startAutoFocus(Rect tfocusRect) {

    }

    @Override
    public void toggleEffect(int a) {
            String number;
            switch (a){
                case 0:
                    number=Camera.Parameters.EFFECT_SEPIA;
                    break;
                case 1:
                    number=Camera.Parameters.EFFECT_MONO;
                    break;
                case 2:
                    number=Camera.Parameters.EFFECT_POSTERIZE;
                    break;
                case 3:
                    number=Camera.Parameters.EFFECT_AQUA;
                    break;
                default:
                    number=Camera.Parameters.EFFECT_NONE;
                    break;
            }

            cameraManager.handleParameters(new ParametersHandler<Camera.Parameters>() {
            @Override
            public Camera.Parameters getParameters(Camera.Parameters cameraParams) {
                cameraParams.setColorEffect(number);
                return cameraParams;
            }
        });
    }

    @Override
    public void startVideoRecord() {
        outputFile = CameraHelper.getOutputMediaFile(cameraView.getActivity(), AnncaConfiguration.MEDIA_ACTION_VIDEO);
        cameraManager.startVideoRecord(outputFile, this);
    }

    @Override
    public void stopVideoRecord() {
        cameraManager.stopVideoRecord();
    }

    @Override
    public boolean isVideoRecording() {
        return cameraManager.isVideoRecording();
    }

    @Override
    public void switchCamera(@AnncaConfiguration.CameraFace final int cameraFace) {
        currentCameraId = cameraManager.getCurrentCameraId().equals(cameraManager.getFaceFrontCameraId()) ?
                cameraManager.getFaceBackCameraId() : cameraManager.getFaceFrontCameraId();

        cameraManager.closeCamera(this);
    }

    @Override
    public void setFlashMode(@AnncaConfiguration.FlashMode int flashMode) {
        cameraManager.setFlashMode(flashMode);
    }

    @Override
    public void switchQuality() {
        cameraManager.closeCamera(this);
    }

    @Override
    public int getNumberOfCameras() {
        return cameraManager.getNumberOfCameras();
    }

    @Override
    public int getMediaAction() {
        return configurationProvider.getMediaAction();
    }

    @Override
    public File getOutputFile() {
        return outputFile;
    }

    @Override
    public Integer getCurrentCameraId() {
        return currentCameraId;
    }


    @Override
    public void onCameraOpened(Integer cameraId, Size previewSize, SurfaceHolder.Callback surfaceCallback) {
        cameraView.updateUiForMediaAction(configurationProvider.getMediaAction());
        cameraView.updateCameraPreview(previewSize, new AutoFitSurfaceView(cameraView.getActivity(), surfaceCallback));
        cameraView.updateCameraSwitcher(getNumberOfCameras());
    }

    @Override
    public void onCameraReady() {
        cameraView.onCameraReady();
    }

    @Override
    public void onCameraOpenError() {
        Log.e(TAG, "onCameraOpenError");
    }

    @Override
    public void onCameraClosed(Integer closedCameraId) {
        cameraView.releaseCameraPreview();

        cameraManager.openCamera(currentCameraId, this);
    }

    @Override
    public void onPhotoTaken(File photoFile) {
        cameraView.onPhotoTaken();
    }

    @Override
    public void onPhotoTakeError() {
    }

    @Override
    public void onVideoRecordStarted(Size videoSize) {
        cameraView.onVideoRecordStart(videoSize.getWidth(), videoSize.getHeight());
    }

    @Override
    public void onVideoRecordStopped(File videoFile) {
        cameraView.onVideoRecordStop();
    }

    @Override
    public void onVideoRecordError() {

    }

    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }
}
