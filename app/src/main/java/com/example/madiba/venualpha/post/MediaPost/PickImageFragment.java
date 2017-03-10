package com.example.madiba.venualpha.post.MediaPost;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adobe.android.ui.view.SquareImageView;
import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.SelectAction;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.ImageUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import it.sephiroth.android.library.picasso.MemoryPolicy;
import it.sephiroth.android.library.picasso.Picasso;
import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PickImageFragment extends Fragment {

    RecyclerView mGalleryRecyclerView;
    TabLayout tabLayout ;
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_JPEG = ".jpeg";
    private static final String EXTENSION_PNG = ".png";
    private static final int MARGING_GRID = 2;

    private MainAdapter mPickerGridAdapter;
    private ArrayList<File> mFiles;
    private boolean isFirstLoad = true;

    static final int REQ_CODE_CAMERA = 1;
    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;
    private File imageFile;
    Uri cameraImageUri;

    public static PickImageFragment newInstance() {
        return new PickImageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_picker, container, false);
        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        mGalleryRecyclerView = (RecyclerView) v.findViewById(R.id.container_recyclerview);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(view -> getActivity().finish());
        initViews();
        return v;
    }



    private void initViews() {
        if (isFirstLoad) {
            mPickerGridAdapter = new MainAdapter(R.layout.picker_item_view,mFiles);
        }
        mGalleryRecyclerView.setAdapter(mPickerGridAdapter);
        mGalleryRecyclerView.setHasFixedSize(true);
        mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mGalleryRecyclerView.addItemDecoration(addItemDecoration());
        mGalleryRecyclerView.setClipToPadding(false);
        mGalleryRecyclerView.setHasFixedSize(true);
        fetchMedia();
    }


    private void initTab(View view){
        tabLayout  = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("adasd"));
        tabLayout.addTab(tabLayout.newTab().setText("adasd"));
        tabLayout.addTab(tabLayout.newTab().setText("adasd"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_camera:
                requestAdobe(cameraImageUri);
                break;
            case R.id.action_next:
                requestPostPage();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }


    //    REQUEST DATA HERE
    //    //////////////////////////////////////////
    private void requestVideo(){

    }

    private void requestCamera(){
        Intent cameraInent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraInent.resolveActivity(getActivity().getPackageManager()) == null) {
            Toast.makeText(getActivity(),"This Application do not have Camera Application",Toast.LENGTH_LONG).show();
            return;
        }

        imageFile = getImageFile();
        cameraInent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(cameraInent, REQ_CODE_CAMERA);
    }

    private void requestPostPage() {
    }

    private void requestAdobe(Uri uri) {
        try {
            Intent imageEditorIntent = new AdobeImageIntent.Builder(getActivity())
                    .setData(uri)
                    .build();
            startActivityForResult(imageEditorIntent, REQ_CODE_CSDK_IMAGE_EDITOR);
        }catch (Exception e){
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_CSDK_IMAGE_EDITOR) {
            if (resultCode == Activity.RESULT_OK) {
                Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
            }
        }
        if (requestCode == REQ_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String realPath;
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 19)
                        realPath = ImageUitls.getRealPathFromURI_API11to18(getApplicationContext(), data.getData());
                    else
                        realPath = ImageUitls.getRealPathFromURI_API19(getApplicationContext(), data.getData());

                }catch (Exception e){
                    Timber.e("erorr getting path for image %s",e.getMessage());
                }
            } else {
                Toast.makeText(getActivity(),"This Application do not have Camera Application",Toast.LENGTH_LONG).show();
            }
        }
    }



    private class MainAdapter extends BaseQuickAdapter<File> {
        private Set<File> checkedSet = new HashSet<>();
        private static final float SELECTED_SCALE = .8f;
        private static final float UNSELECTED_SCALE = 1f;
        MainAdapter(int layoutResId, List<File> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, File file) {

            SquareImageView itemView = holder.getView(R.id.image_view);

            Picasso.with(getContext())
                    .load(Uri.fromFile(file))
                    .resize(120, 120)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .into(itemView);


            boolean selected ;
            if(checkedSet.contains(file)){
                selected = true;

            }else {
                selected = false;
            }
            if (selected) {
                itemView.setScaleX(SELECTED_SCALE);
                itemView.setScaleY(SELECTED_SCALE);
            } else {
                itemView.setScaleX(UNSELECTED_SCALE);
                itemView.setScaleY(UNSELECTED_SCALE);
            }

            itemView.setOnClickListener(view -> {
                if (checkedSet.contains(file)) {
                    checkedSet.add(file);
                } else {
                    checkedSet.remove(file);
                }
                notifyItemChanged(getItemCount());
                notifyDataSetChanged();
            });
        }

        public Set<File> returnData(){
            return checkedSet;
        }

    }

    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }
    private void fetchMedia() {
        mFiles = new ArrayList<>();
//        File dirDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        parseDir(dirDownloads);
        fetchDownloads();
//        File dirDcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        parseDir(dirDcim);
//        File dirPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        parseDir(dirPictures);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            File dirDocuments = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//            parseDir(dirDocuments);
//        }

        if (mFiles.size() > 0) {
            mPickerGridAdapter.setNewData(mFiles); // getRangePets()
        }
        isFirstLoad = false;
    }

    private void fetchDownloads() {
        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

            imageCursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            //imageCursor = sContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);


            if (imageCursor != null) {

                while (imageCursor.moveToNext()) {
                    String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(imageLocation);
                    mFiles.add(file);
                }

            }
            imageCursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (imageCursor != null) {
                while (imageCursor.moveToNext()) {
                    String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(imageLocation);
                    mFiles.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }

    }

    private void parseDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            parseFileList(files);
        }
    }

    private void parseFileList(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().toLowerCase().startsWith(".")) {
                    parseDir(file);
                }
            } else {
                if (file.getName().toLowerCase().endsWith(EXTENSION_JPG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_JPEG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_PNG)) {
                    mFiles.add(file);
                }
            }
        }
    }


    private File getImageFile() {
        // Create an image file name
        File imageFile = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


            imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );


            // Save a file: path for use with ACTION_VIEW intents
            cameraImageUri = Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return imageFile;
    }




    @Override
    public void onPause() {
        super.onPause();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectAction action) {
//        if (action.filSelectActione != null) {
//            pickerSession.setFileToUpload(action.file.getAbsolutePath());
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}