package com.example.madiba.venualpha.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import timber.log.Timber;

import static android.content.ContentResolver.SCHEME_CONTENT;

/**
 * Created by Madiba on 11/23/2016.
 */

public class ImageUitls {

    public static final int BYTE = 1;

    public static final int KB = 1024;

    public static final int MB = 1048576;

    public static final int GB = 1073741824;

    public enum MemoryUnit {
        BYTE,
        KB,
        MB,
        GB
    }

    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";



    public ImageUitls() {}

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws Exception {

        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        Timber.i("handleSamplingAndRotationBitmap : STARTING  ");

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Timber.i("handleSamplingAndRotationBitmap : DECODED  ");

//        InputStream imageStream = context.getContentResolver().openInputStream(Uri.parse(Session.getInstance().getFileToUpload()));
//        BitmapFactory.decodeStream(imageStream, null, options);
        Bitmap bitmap= BitmapFactory.decodeFile(selectedImage.getPath(),options);
//        imageStream.close();
        Timber.i("handleSamplingAndRotationBitmap : OUT OF MEMEORY CHECK  ");

        // Calculate inSampleSizecalculateInSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
//        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeFile(selectedImage.getPath(), options);
        Timber.i("handleSamplingAndRotationBitmap : RETURN BITMAP  ");

        img = rotateImageIfRequired(img, selectedImage);
        Timber.i("handleSamplingAndRotationBitmap : RETURNING ROTATED IMAGES");
        return img;

    }


    public static long size2Byte(long size, MemoryUnit unit) {


        switch (unit) {
            default:
            case BYTE:
                return size * BYTE;
            case KB:
                return size * KB;
            case MB:
                return size * MB;
            case GB:
                return size * GB;
        }

    }

    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format,int topLimit) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, topLimit, baos);
        return baos.toByteArray();
    }

    public static Bitmap compress(Bitmap src, Bitmap.CompressFormat format, long topLimit, MemoryUnit unit) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, 100, os);
        long upperSize = size2Byte(topLimit, unit);
        while (os.toByteArray().length > upperSize) {
            os.reset();
            src.compress(format, 50, os);
        }
        if (!src.isRecycled()) src.recycle();
        return BitmapFactory.decodeStream(new ByteArrayInputStream(os.toByteArray()));
    }

    public static Bitmap rotateBitmap(Bitmap src, int degrees) {
        if (src == null || degrees == 0) return src;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, src.getWidth() / 2, src.getHeight() / 2);
        Bitmap res = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (!src.isRecycled()) src.recycle();
        return res;
    }

    public static Bitmap notificationResize(Context context, Bitmap currentImage) {
        try {
            int scale = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics());

            return Bitmap.createScaledBitmap(currentImage, scale, scale, true);
        } catch (OutOfMemoryError e) {
            return currentImage;
        }
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
            Timber.e(e.getMessage());
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


    public byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public static Bitmap overlayPlay(Bitmap bmp1, Context context,int resId) {
        Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), resId, null);

        Bitmap bmOverlay = Bitmap.createBitmap( bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bmOverlay);
        canvas2.drawBitmap(bmp1, 0, 0, null);
//        canvas2.drawBitmap(bmp2, Utils.toDP(64, context), Utils.toDP(64, context), null);
        return bmOverlay;
    }

    public static boolean deleteFile(Context context, String fileName)
    {
        return context.deleteFile(fileName);
    }


    public static boolean exists(Context context, String fileName)
    {
        return new File(context.getFilesDir(), fileName).exists();
    }

    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth(); // 获取位图的宽
        int height = img.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }


    public static Drawable tint(Drawable input, ColorStateList tint) {
        if (input == null) {
            return null;
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(input);
        DrawableCompat.setTintList(wrappedDrawable, tint);
        DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.MULTIPLY);
        return wrappedDrawable;
    }

    public static Drawable tint(Drawable input) {
        if (input == null) {
            return null;
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(input);
        DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.MULTIPLY);
        return wrappedDrawable;
    }

    private @Nullable static
    Drawable getTinted(Context context,@DrawableRes int res, @ColorInt int color) {
        // need to mutate otherwise all references to this drawable will be tinted
        Drawable drawable = ContextCompat.getDrawable(context, res).mutate();
        return tint(drawable, ColorStateList.valueOf(color));
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }

        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    public static boolean shouldRotate(ContentResolver resolver, Uri uri) {
//        ExifInterface exif;
////        try {
////            exif = ExifInterfaceCompat.newInstance(getPath(resolver, uri));
////        } catch (IOException e) {
////            Timber.e("could not read exif info of the image: " + uri);
////            return false;
////        }
//        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
//        return orientation == ExifInterface.ORIENTATION_ROTATE_90
//                || orientation == ExifInterface.ORIENTATION_ROTATE_270;
        return true;
    }

    public static String getPath(ContentResolver resolver, Uri uri) {
        if (uri == null) {
            return null;
        }

        if (SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            } finally {
                if(cursor != null)
                    cursor.close();
            }
        }
        return uri.getPath();
    }
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @SuppressLint("NewApi")
    public static String getPathResolver(final Context context, final Uri uri) {



        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException ex) {
          Timber.e("getDataColumn: _data %s", ex.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @return
     * @author paulburke
     */


    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {

        if (!isMediaUri(uri)) {
            Timber.e("You can only retrieve thumbnails for images and videos.");
            return null;
        }

        Bitmap bm = null;
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);


                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Video.Thumbnails.MINI_KIND,
                                null);
                    } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Images.Thumbnails.MINI_KIND,
                                null);
                    }
                }
            } catch (Exception e) {
                Timber.e("getThumbnail %s", e.getMessage());
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return bm;
    }

    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    public static String getMimeType(File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

}
