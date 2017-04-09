package io.github.memfis19.annca.internal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by memfis on 7/18/16.
 */
public class Utils {

    public static int getDeviceDefaultOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Configuration config = context.getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }

    public static String getMimeType(String url) {
        String type = "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (!TextUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        } else {
            String reCheckExtension = MimeTypeMap.getFileExtensionFromUrl(url.replaceAll("\\s+", ""));
            if (!TextUtils.isEmpty(reCheckExtension)) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(reCheckExtension);
            }
        }
        return type;
    }

    public static int convertDipToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }


    public static String getPath(Uri uri, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String filePath ;


        try {
            Cursor cursor = context.getContentResolver().query(
                    uri, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            filePath = uri.getPath();
        }

        if (filePath == null) {
            filePath = uri.getPath();
            Log.v("talon_file_path", filePath);
        }

        return filePath;
    }

    public static String getImageContentUri(Context context, File imageFile) {
        Uri returnPath ;
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            returnPath= Uri.withAppendedPath(baseUri, "" + id);
            return returnPath.toString();
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                returnPath = context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                return returnPath != null ? returnPath.toString() : null;
            } else {
                return null;
            }
        }}


}
