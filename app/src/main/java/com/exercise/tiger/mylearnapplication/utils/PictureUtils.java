package com.exercise.tiger.mylearnapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片处理工具类
 * Created by hzj on 2017/3/15.
 */

public class PictureUtils {
    private static final long fileMaxSize = 200 * 1024;
    private static final int quantity =100;
    public static String getFilePathAfterScale(Uri fileUri, Context context){
        String path = fileUri.getPath();
        File outputFile = new File(path);
        Bitmap origin = BitmapFactory.decodeFile(path);
        long fileSize = getBitmapSize(origin);
        origin.recycle();
        if (fileSize > fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile(context).getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, quantity, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.d(, sss ok  + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }else{
                File tempFile = outputFile;
                outputFile = new File(createImageFile(context).getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile.getPath();

    }

    public static Uri createImageFile(Context context){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }
    public static void copyFileUsingFileChannels(File source, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Uri getUriFromFile(Context context,File file){
        Uri uri;
        if (BuildUtil.hasNougat()){
            uri = FileProvider.getUriForFile(context,"com.whfitc.dragon.provider",file);
        }else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static long getBitmapSize(Bitmap bitmap){
        if (BuildUtil.hasKITKAT()){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (BuildUtil.hasHoneycombMR1()){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
