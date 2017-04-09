package com.example.madiba.venualpha.post.MediaPost;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.util.ImageUitls;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;
import it.sephiroth.android.library.picasso.MemoryPolicy;
import it.sephiroth.android.library.picasso.Picasso;
import timber.log.Timber;

import static com.example.madiba.venualpha.util.ImageUitls.createImageFile;


public class PickerFragment extends Fragment {

    private static final int MARGING_GRID = 2;
    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;
    static final int REQ_CODE_CAMERA = 1001;
    static final int REQ_CODE_VIDEO = 2001;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    static final int REQ_CODE_FLIPSIDE = 4001;
    private ImageView mClose, mCamera, mVideo, mFlipSide, mNext;
    private RecyclerView mHeaderRview, mMainRview;
    private TextView mCount, mEditText;
    private ArrayList<VenuFile> mFiles;
    private int editingIndex;
    private HeaderAdapter headerAdapter;
    private MainAdapter mainAdapter;
    private OnFragmentInteractionListener mListener;
    private boolean isFirstLoad = true;
    private File imageFile;
    private Uri cameraImageUri;
    private Uri imageUri;
    private VenuFile editingVenufile;
    private Fragment fragment;

    private List<VenuFile> mHeaderFiles = new ArrayList<>();

    public PickerFragment() {
        // Required empty public constructor
    }

    public static PickerFragment newInstance() {

        return new PickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picker_version2, container, false);
        mClose = (ImageView) view.findViewById(R.id.close);
        mCamera = (ImageView) view.findViewById(R.id.camera);
        mVideo = (ImageView) view.findViewById(R.id.video);
        mFlipSide = (ImageView) view.findViewById(R.id.flipside);
        mCount = (TextView) view.findViewById(R.id.count);
        mHeaderRview = (RecyclerView) view.findViewById(R.id.head_rcview);
        mMainRview = (RecyclerView) view.findViewById(R.id.main_rcview);
        mNext = (ImageView) view.findViewById(R.id.next);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCount.setText("0/0");
        displayToolbar();
        displayHeader();
        fetchMedia();
    }


/////////////////////////////////////////////////////
    //    RESTORE STATE HERE    //
///////////////////////////////////////////////////


/////////////////////////////////////////////////////
    //    INITIALISING   //
///////////////////////////////////////////////////


    public void displayToolbar() {
        mClose.setOnClickListener(view -> getActivity().finish());

        mCamera.setOnClickListener(view -> requestCamera());

        mVideo.setOnClickListener(view -> requestVideo());

        mFlipSide.setOnClickListener(view -> requestFlipside());

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (headerAdapter.getItemCount() > 0)
                        mListener.multImage(headerAdapter.getData());
                }
            }
        });
    }


    public void displayHeader() {
        headerAdapter = new HeaderAdapter(R.layout.item_venu_picker, mHeaderFiles);
        mHeaderRview.setAdapter(headerAdapter);
        mHeaderRview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHeaderRview.setHasFixedSize(true);
    }


    public void displayMainAdapter(List<VenuFile> venuFiles) {
        mainAdapter = new MainAdapter(venuFiles.size(), venuFiles);
        mMainRview.setAdapter(mainAdapter);
        mMainRview.setHasFixedSize(true);
        mMainRview.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        mMainRview.addItemDecoration(addItemDecoration());
        mMainRview.setClipToPadding(false);
        mMainRview.setHasFixedSize(true);
    }


    /////////////////////////////////////////////////////
    //    OPERATION HERE   //
    /////////////////////////////////////////////////////


    private void reload() {

    }

    private void requestEditor(VenuFile venuFile) {
        try {
            Intent imageEditorIntent = new AdobeImageIntent.Builder(getActivity())
                    .setData(Uri.parse(venuFile.getUrl()))
                    .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                    .withOutputSize(MegaPixels.Mp5) // output size
                    .withOutputQuality(90) // output quality
                    .build();

            startActivityForResult(imageEditorIntent, REQ_CODE_CSDK_IMAGE_EDITOR);
        } catch (Exception e) {
        }
    }

    private void requestAdd(VenuFile venuFile) {
        Log.e("PICKER", "got image file" + venuFile.toString());
        if (headerAdapter.getItemCount() < 4) {
            headerAdapter.add(0, venuFile);
            headerAdapter.notifyDataSetChanged();
            mHeaderRview.scrollToPosition(0);

            mCount.setText(headerAdapter.getItemCount() + "/4");
        }
    }

    private void requestRemove(int position) {
        headerAdapter.remove(position);
        headerAdapter.notifyDataSetChanged();
    }

    private void requestUpdate(int index, VenuFile venuFile) {
        headerAdapter.updateItem(venuFile, index);
        headerAdapter.notifyDataSetChanged();
    }


    private void requestFlipside() {
        startActivityForResult(new Intent(getActivity(), ActivityFlipsideActivity.class), REQ_CODE_FLIPSIDE);
    }

    private void requestVideo() {

        AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(fragment, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
        videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_AUTO);
        videoLimited.setVideoFileSize(15 * 1024 * 1024);
        videoLimited.setMinimumVideoDuration(5 * 1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        new Annca(videoLimited.build()).launchCamera();

    }

    private void requestCamera() {

        imageFile = createImageFile();
        imageUri = Uri.fromFile(imageFile);

        if (imageUri != null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                Toast.makeText(getActivity(), "This Application do not have Camera Application", Toast.LENGTH_LONG).show();
                return;
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, REQ_CODE_CAMERA);
        }
    }


/////////////////////////////////////////////////////
    //    ONACTIVIY RESULT DATA HERE   //
///////////////////////////////////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_CSDK_IMAGE_EDITOR) {
            if (resultCode == Activity.RESULT_OK) {
                Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                try {
                    String realPath;
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = ImageUitls.getRealPathFromURI_BelowAPI11(getActivity().getApplicationContext(), editedImageUri);

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = ImageUitls.getRealPathFromURI_API11to18(getActivity().getApplicationContext(), editedImageUri);

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = ImageUitls.getRealPathFromURI_API19(getActivity().getApplicationContext(), editedImageUri);

                    requestRemove(editingIndex);
                    requestAdd(new VenuFile(realPath, 1, false));
                } catch (Exception e) {
                    Timber.e("erorr getting path for image %s", e.getMessage());
                }
            }
        } else if (requestCode == REQ_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    requestAdd(new VenuFile(imageUri.getPath(), 1, false));
                } catch (Exception e) {
                    Timber.e("erorr getting path for image %s", e.getMessage());
                }
            } else {
                Toast.makeText(getActivity(), "This Application do not have Camera Application", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQ_CODE_FLIPSIDE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    VenuFile a = new VenuFile(data.getData().toString(), 1, false);
                    Log.e("PICKER", "onActivityResult: path" + a.getUrl());

                } catch (Exception e) {
                    Timber.e("erorr getting path for image %s", e.getMessage());
                }
            } else {
                Toast.makeText(getActivity(), "This Application do not have Camera Application", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("PICKER", "onActivityResult:" + data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH));
                mListener.singleVideo(new VenuFile(data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH), 0, false));
            } else {
                Log.e("PICKER", "onActivityResult: not return");
            }
        }
    }


//////////////////////////////////////////////////////////
    //    Data loaders here  //
//////////////////////////////////////////////////////////

    public class HeaderAdapter extends BaseQuickAdapter<VenuFile> {

        public HeaderAdapter(int layoutResId, List<VenuFile> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, VenuFile file) {
            RoundCornerImageView roundCornerImageView = (holder.getView(R.id.avatar));
            ImageButton close = holder.getView(R.id.close);
            TextView edit = holder.getView(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editingIndex = mData.indexOf(file);
                    editingVenufile = file;
                    requestEditor(file);
                }
            });

            close.setOnClickListener(view -> {
                remove(getData().indexOf(file));
                notifyDataSetChanged();
                mCount.setText(headerAdapter.getItemCount() + "/4");

            });

            Picasso.with(getContext())
                    .load(Uri.parse(file.getUrl()))
                    .resize(120, 120)
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .into(roundCornerImageView);
        }


        public void updateItem(VenuFile venuFile, int position) {
            notifyItemChanged(position, venuFile);
            notifyDataSetChanged();
        }
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {

        com.example.madiba.venualpha.ui.SquareImageView avatar;

        MainViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_gallery, parent, false));
            avatar = (com.example.madiba.venualpha.ui.SquareImageView) itemView.findViewById(R.id.image_view);
        }

    }

    private class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {
        private List<VenuFile> data;
        private final int mItemCount;

        MainAdapter(int itemCount, List<VenuFile> data) {
            mItemCount = itemCount;
            this.data = data;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {

            VenuFile item = this.data.get(position);
            Picasso.with(getContext())
                    .load(item.getUrl())
                    .resize(150, 150)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .centerCrop()
                    .into(holder.avatar);
            holder.avatar.setOnClickListener(v -> {
                requestAdd(item);
            });
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

    private void fetchMedia() {
        mFiles = new ArrayList<>();
        fetchDownloads();


        if (mFiles.size() > 0) {
            displayMainAdapter(mFiles);
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
                    mFiles.add(new VenuFile(imageLocation, GlobalConstants.VENUFILE_DEFAULT, false));
                }
            }
            imageCursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (imageCursor != null) {
                while (imageCursor.moveToNext()) {
                    String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    mFiles.add(new VenuFile(imageLocation, GlobalConstants.VENUFILE_DEFAULT, false));
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void multImage(List<VenuFile> venuFiles);

        void singleImage(VenuFile venuFile);

        void singleVideo(VenuFile venuFile);
    }
}
