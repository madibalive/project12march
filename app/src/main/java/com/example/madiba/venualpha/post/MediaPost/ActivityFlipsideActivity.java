package com.example.madiba.venualpha.post.MediaPost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.SwipeImageView;
import com.example.madiba.venualpha.ui.venubutton.AllAngleExpandableButton;
import com.example.madiba.venualpha.ui.venubutton.ButtonData;
import com.example.madiba.venualpha.ui.venubutton.ButtonEventListener;
import com.example.madiba.venualpha.util.FlipUtil;
import com.example.madiba.venualpha.util.ImageUitls;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.ImageView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class ActivityFlipsideActivity extends AppCompatActivity {

    private RelativeLayout activityMain;
    private CameraView cameraPreview;
    private LinearLayout topPicLayout;
    private FrameLayout topPicImageLayout;
    private SwipeImageView topPic;
    private ProgressBar topPicProgress;
    private FrameLayout topBottomPicImageLayout;
    private SwipeImageView topBottomPic;
    private ProgressBar topBottomPicProgress;
    private LinearLayout bottomPicLayout;
    View topEmptyPic;
    private ImageView bottomTopPic;
    private ImageView bottomPic;
    private FloatingActionButton capturePhotoButton;

    private Toolbar toolbar;
    AllAngleExpandableButton button;


    private File fileTopImage, fileBottomImage;
    private MenuItem helpActionItem, flashActionItem;
    private boolean isTopPictureTaken;
    private boolean isReverseImageOrder;
    private boolean hasPermissiongranted;
    private int transformNumberTop, transformNumberBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

            getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_flipside);

        activityMain = (RelativeLayout) findViewById(R.id.activity_main);
        cameraPreview = (CameraView) findViewById(R.id.camera_preview);
        topPicLayout = (LinearLayout) findViewById(R.id.top_pic_layout);
        topPicImageLayout = (FrameLayout) findViewById(R.id.top_pic_image_layout);
        topPic = (SwipeImageView) findViewById(R.id.top_pic);
        topPicProgress = (ProgressBar) findViewById(R.id.top_pic_progress);
        topBottomPicImageLayout = (FrameLayout) findViewById(R.id.top_bottom_pic_image_layout);
        topBottomPic = (SwipeImageView) findViewById(R.id.top_bottom_pic);
        topBottomPicProgress = (ProgressBar) findViewById(R.id.top_bottom_pic_progress);
        bottomPicLayout = (LinearLayout) findViewById(R.id.bottom_pic_layout);
        bottomTopPic = (ImageView) findViewById(R.id.bottom_top_pic);
        bottomPic = (ImageView) findViewById(R.id.bottom_pic);
        capturePhotoButton = (FloatingActionButton) findViewById(R.id.capture_photo_button);
        topEmptyPic = (View) findViewById(R.id.top_empty_pic);
         button = (AllAngleExpandableButton) findViewById(R.id.circle_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, FlipUtil.getStatusBarHeightNew(this), 0, 0);
        toolbar.setLayoutParams(params);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        topPic.setSwipeListener(new SwipeImageView.OnSwipeListener() {
            @Override
            public void onSwipeRight() {
                imageEffectRight(topPic, isReverseImageOrder ? fileBottomImage : fileTopImage, true);
            }

            @Override
            public void onSwipeLeft() {
                imageEffectLeft(topPic, isReverseImageOrder ? fileBottomImage : fileTopImage, true);
            }
        });
        topPic.setIsSwipeEnabled(false);
        topBottomPic.setSwipeListener(new SwipeImageView.OnSwipeListener() {
            @Override
            public void onSwipeRight() {
                imageEffectRight(topBottomPic, isReverseImageOrder ? fileTopImage : fileBottomImage, false);
            }

            @Override
            public void onSwipeLeft() {
                imageEffectLeft(topBottomPic, isReverseImageOrder ? fileTopImage : fileBottomImage, false);
            }
        });
        topBottomPic.setIsSwipeEnabled(false);
        capturePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });
        setUpFab();
    }

    private View getTopEmptyPic(){
        return (View) findViewById(R.id.top_empty_pic);
    }

    private View getBottomEmptyPic(){
        return (View) findViewById(R.id.bottom_empty_pic);
    }


    private void setUpFab() {
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.ic_panorama, R.drawable.ic_cloud, R.drawable.ic_live_now, R.drawable.ic_camera, R.drawable.ic_event};
        int[] color = {R.color.transparent, R.color.white, R.color.transparent, R.color.transparent, R.color.white};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 7);
            } else {
                buttonData = ButtonData.buildIconButton(this, drawable[0], 0);
            }
            buttonData.setBackgroundColorId(this, color[i]);
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);
        setListener(button);

    }


    private void setListener(AllAngleExpandableButton button) {


        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index){
                    case 3:
                        Timber.i("FAB CLICK BUTTON %S",3);
                        clearImages();

                        break;
                    case 2:
                        Timber.i("FAB CLICK BUTTON %S",2);
                        swapImages();

                        break;
                    case 1:
                        Timber.i("FAB CLICK BUTTON %S",1);
                        shareImage();

                        break;
                    case 4:
                        Timber.i("FAB CLICK BUTTON %S",index);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onExpand() {
            }

            @Override
            public void onCollapse() {
            }
        });
    }


    private void shareImage() {
        topPic.buildDrawingCache();
        Bitmap topBitmap = topPic.getDrawingCache();
        topBottomPic.buildDrawingCache();
        Bitmap bottomBitmap = topBottomPic.getDrawingCache();
        String pathToCombinedImage = FlipUtil.combineBitmaps(this, topBitmap, bottomBitmap);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(new File(pathToCombinedImage));

        Log.e("FLIPSIDE", "shareImage: path"+uri.getPath() );
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(shareIntent, "Share via"));
        returnBack(uri);
    }

    private void returnBack(Uri uri) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("imageUrl",uri);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }






    void capturePhoto() {
        cameraPreview.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

                //show pictures and change view state
                if (isTopPictureTaken) {

                    saveBottomImage(bitmap);

                } else {

                    saveTopImage(bitmap);

                }
            }
        });
        cameraPreview.captureImage();
    }

    private void imageEffectLeft(SwipeImageView swipeImageView, File imageFile, final boolean isTop) {
        if (isTop) {
            if (transformNumberTop >= 1) {
                transformNumberTop -= 1;
            }
        } else {
            if (transformNumberBottom >= 1) {
                transformNumberBottom -= 1;
            }
        }
        setProgressVisibility(isTop, true);
        FlipUtil.transformImage(getApplicationContext(), isTop ? transformNumberTop : transformNumberBottom,
                swipeImageView, imageFile, new Callback() {
                    @Override
                    public void onSuccess() {
                        setProgressVisibility(isTop, false);
                    }

                    @Override
                    public void onError() {
                        setProgressVisibility(isTop, false);
                    }
                });
    }

    private void imageEffectRight(SwipeImageView swipeImageView, File imageFile, final boolean isTop) {
        if (isTop) {
            if (transformNumberTop <= 9) {
                transformNumberTop += 1;
            }
        } else {
            if (transformNumberBottom <= 11) {
                transformNumberBottom += 1;
            }
        }
        setProgressVisibility(isTop, true);
        FlipUtil.transformImage(getApplicationContext(), isTop ? transformNumberTop : transformNumberBottom,
                swipeImageView, imageFile, new Callback() {
                    @Override
                    public void onSuccess() {
                        setProgressVisibility(isTop, false);
                    }

                    @Override
                    public void onError() {
                        setProgressVisibility(isTop, false);
                    }
                });
    }

    private void setProgressVisibility(boolean isTop, boolean isVisible) {
        if (isTop) {
            topPicProgress.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        } else {
            topBottomPicProgress.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void saveBottomImage(Bitmap bitmap) {
        String pathToBottomImage = FlipUtil.halfBitmap(this, false, bitmap);
        fileBottomImage = new File(pathToBottomImage);
        bitmap.recycle();

        Picasso.with(this).load(fileBottomImage).fit().centerCrop().noPlaceholder().into(topBottomPic);

        setTopLayoutImageVisibility(true);

        showActionButtonsVisibility(true);

        topPic.setIsSwipeEnabled(true);
        topBottomPic.setIsSwipeEnabled(true);

        //only show help the first time the user takes pic and then they can access from
        //action bar button
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (!(sharedPref.getBoolean(getString(R.string.has_help_shown_key), false))) {
            showImageEffectsHelp();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.has_help_shown_key), true);
            editor.apply();
        }
    }

    private void saveTopImage(Bitmap bitmap) {
        String pathToTopImage = FlipUtil.halfBitmap(this, true, bitmap);
        bitmap.recycle();
        fileTopImage = new File(pathToTopImage);

        Picasso.with(this).load(fileTopImage).fit().centerCrop().noPlaceholder().into(topPic);

        switchCameraViews(false);
    }

    /**
     * used to show the image effect action help screen
     */
    private void showImageEffectsHelp() {
        final Display display = getWindowManager().getDefaultDisplay();
        final Drawable arrows = ContextCompat.getDrawable(this, R.drawable.ic_compare_arrows_black_24dp);
        final Rect arrowsTargetTop = new Rect(0, 0, arrows.getIntrinsicWidth() * 2, arrows.getIntrinsicHeight() * 2);
        arrowsTargetTop.offset(display.getWidth() / 2, display.getHeight() / 4);
        final Rect arrowsTargetBottom = new Rect(0, 0, arrows.getIntrinsicWidth() * 2, arrows.getIntrinsicHeight() * 2);
        arrowsTargetBottom.offset(display.getWidth() / 2, ((display.getHeight() / 4)*3));
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.circle_menu), getString(R.string.action_button_help_title),
                                getString(R.string.action_button_help_text)).transparentTarget(true),
                        TapTarget.forBounds(arrowsTargetTop, getString(R.string.top_image_help_title),
                                getString(R.string.top_image_help_text)).icon(arrows),
                        TapTarget.forBounds(arrowsTargetBottom, getString(R.string.bottom_image_help_title),
                                getString(R.string.bottom_image_help_text)).icon(arrows)).start();
    }

    private void toggleFlash() {
        switch (cameraPreview.toggleFlash()) {
            case CameraKit.Constants.FLASH_ON:
                if (flashActionItem!=null) {
                    flashActionItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_flash_on));
                }
                break;
            case CameraKit.Constants.FLASH_OFF:
                if (flashActionItem!=null) {
                    flashActionItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_flash_off));
                }
                break;
            case CameraKit.Constants.FLASH_AUTO:
                if (flashActionItem!=null) {
                    flashActionItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_flash_auto));
                }
                break;
        }
    }

    private void toggleFlashAuto() {
        cameraPreview.setFlash(CameraKit.Constants.FLASH_AUTO);
        if (flashActionItem!=null) {
            flashActionItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_flash_auto));
        }
    }

    private void setTopLayoutImageVisibility(boolean hasBothImagesVisible) {
        try {
            topEmptyPic.setVisibility(hasBothImagesVisible ? View.GONE : View.VISIBLE);
            topBottomPicImageLayout.setVisibility(hasBothImagesVisible ? View.VISIBLE : View.GONE);

        }catch (Exception e){

        }

    }

    /**
     * set second image and show image acton buttons
     */
    private void showActionButtonsVisibility(boolean isActionButtonsVisible) {
        capturePhotoButton.setVisibility(isActionButtonsVisible ? View.GONE : View.VISIBLE);
        button.setVisibility(isActionButtonsVisible ? View.VISIBLE : View.GONE);
        helpActionItem.setVisible(isActionButtonsVisible);
    }

    /**
     * method to switch between the top and bottom camera views
     */
    public void switchCameraViews(boolean isTopCameraView) {
        this.isTopPictureTaken = !isTopCameraView;
        topPicLayout.setVisibility(isTopCameraView ? View.GONE : View.VISIBLE);
        bottomPicLayout.setVisibility(isTopCameraView ? View.VISIBLE : View.GONE);
        flashActionItem.setVisible(isTopCameraView);
        //set or remove images as required
        if (isTopCameraView) {
            //remove images as resetting view to top
            topPic.setImageBitmap(null);
            topBottomPic.setImageBitmap(null);
            //change visibility of views
            capturePhotoButton.setVisibility(View.VISIBLE);
            setTopLayoutImageVisibility(false);
            // switch flash back on to auto
            toggleFlashAuto();
        }

        //change to back camera
        cameraPreview.toggleFacing();
    }

    /**
     * circular fab action button methods
     */

    private void clearImages() {
        transformNumberTop = 0;
        transformNumberBottom = 0;
        topPic.setIsSwipeEnabled(false);
        topBottomPic.setIsSwipeEnabled(false);
        FlipUtil.cleanUpTempDir(this);
        showActionButtonsVisibility(false);
        switchCameraViews(true);
    }

    private void swapImages() {
        if (isReverseImageOrder) {
            FlipUtil.transformImage(getApplicationContext(), transformNumberBottom,
                    topPic, fileTopImage, null);
            FlipUtil.transformImage(getApplicationContext(), transformNumberTop,
                    topBottomPic, fileBottomImage, null);

            isReverseImageOrder = false;
        } else {
            FlipUtil.transformImage(getApplicationContext(), transformNumberBottom,
                    topBottomPic, fileTopImage, null);
            FlipUtil.transformImage(getApplicationContext(), transformNumberTop,
                    topPic, fileBottomImage, null);

            isReverseImageOrder = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_flipside, menu);

        helpActionItem = menu.findItem(R.id.help_item);
        helpActionItem.setVisible(false);
        flashActionItem = menu.findItem(R.id.flash_item);
        toggleFlashAuto();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.about_item) {
        } else if (item.getItemId() == R.id.help_item) {
            showImageEffectsHelp();
        } else if (item.getItemId() == R.id.flash_item) {
            toggleFlash();
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onResume() {
        super.onResume();
            cameraPreview.start();

    }

    @Override
    protected void onPause() {
        cameraPreview.stop();
        super.onPause();
    }
}
