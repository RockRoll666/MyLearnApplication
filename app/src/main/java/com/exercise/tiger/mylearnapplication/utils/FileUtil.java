package com.exercise.tiger.mylearnapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.exercise.tiger.mylearnapplication.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;

public class FileUtil {

    public static String WORK_DIRECTORY = Environment.getExternalStorageDirectory() + "/Android/data/"+ BuildConfig.APPLICATION_ID +"/files/";

    public static String WORK_DIRECTORY_CAPTURE = WORK_DIRECTORY + ".camera/";

    public static String WORK_DIRECTORY_VIDEO = WORK_DIRECTORY + "video/";

    public static String WORK_DIRECTORY_AUDIO = WORK_DIRECTORY + "audio/";

    public static String WORK_DIRECTORY_CACHE = WORK_DIRECTORY + "cache/";

    public static String WORK_DIRECTORY_CACHE_IMAGE = WORK_DIRECTORY_CACHE + "image/";

    public static String WORK_DIRECTORY_DOWNLOAD = WORK_DIRECTORY + "download/";

    public static String WORK_DIRECTORY_IMAGE = WORK_DIRECTORY + "image/";

    /**
     * 对文件中的某一部分的数据进行加密
     *
     * @param fName :要修改的文件名字
     * @param start :起始字节
     * @param len   :要修改多少个字节
     *
     * @return :是否修改成功
     *
     * @throws Exception :文件读写中可能出的错
     */
    public static boolean encryptFile(String fName, int start, int len) throws Exception {
        java.io.RandomAccessFile raf = new java.io.RandomAccessFile(fName, "rw");
        long totalLen = raf.length();
        FileChannel channel = raf.getChannel();

        java.nio.MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, start, len);

        byte[] original = new byte[len];
        for (int i = 0; i < len; i++) {
            original[i] = buffer.get(i);
        }

        byte[] encrypt = AESUtil.encrypt(original, "12345678");

        for (int i = 0; i < len; i++) {
            byte src = encrypt[i];
            buffer.put(i, src);
        }

        buffer.force();
        buffer.clear();
        channel.close();
        raf.close();
        return true;
    }

    /**
     * 对文件中的某一部分的数据进行解密
     *
     * @param fName :要修改的文件名字
     * @param start :起始字节
     * @param len   :要修改多少个字节
     *
     * @return :是否修改成功
     *
     * @throws Exception :文件读写中可能出的错
     */
    public static boolean decryptFile(String fName, int start, int len) throws Exception {
        java.io.RandomAccessFile raf = new java.io.RandomAccessFile(fName, "rw");
        long totalLen = raf.length();

        FileChannel channel = raf.getChannel();

        java.nio.MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, start, len);

        byte[] encrypt = new byte[len];
        for (int i = 0; i < len; i++) {
            encrypt[i] = buffer.get(i);
        }

        // Debug.print("encrypt2 string:" + Arrays.toString(encrypt));

        byte[] original = AESUtil.decrypt(encrypt, "12345678");

        // Debug.print("original2 string:" + Arrays.toString(original));

        for (int i = 0; i < len; i++) {
            byte src = original[i];
            buffer.put(i, src);
        }

        buffer.force();
        buffer.clear();
        channel.close();
        raf.close();
        return true;
    }

    public static void copyFile(InputStream is, OutputStream os) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte[] buffer = new byte[512];
        try {
            bis = new BufferedInputStream(is, 8192);
            bos = new BufferedOutputStream(os, 8192);
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 得到本地文件保存路径
     *
     * @return
     */
    public static String getSaveDir(Context context) {

        String fileDir;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
            File f = context.getExternalCacheDir();
            if (null == f) {
                fileDir = Environment.getExternalStorageDirectory().getPath() + File.separator
                        + context.getPackageName() + File.separator + "cache";
            } else {
                fileDir = f.getPath();
            }
        } else {
            File f = context.getCacheDir();
            fileDir = f.getPath();
        }
        return fileDir;
    }

    /**
     * 将服务器的返回值保存至文件中
     *
     * @param fileSavePath
     * @param result
     */
    public static void saveFileForLocal(String fileSavePath, String result) {
        String path = fileSavePath.substring(0, fileSavePath.lastIndexOf("/"));
        String fileName = fileSavePath.substring(fileSavePath.lastIndexOf("/"), fileSavePath.length());
        File file = new File(path, fileName);

        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fout = new FileOutputStream(file);
            byte[] buffer = result.getBytes();
            fout.write(buffer);
            fout.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void deleteFileFromLocal(String fileSavePath) {
        File file = new File(fileSavePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 从文件中读取字符串
     *
     * @param
     *
     * @return
     */
    @SuppressWarnings("resource")
//    public static String getFileFromLocal(String fileSavePath) {
//        File file = new File(fileSavePath);
//        String result = "";
//        if (file.exists()) {
//            FileInputStream fileIn;
//            try {
//                fileIn = new FileInputStream(file);
//                int length = fileIn.available();
//                byte[] buffer = new byte[length];
//                fileIn.read(buffer);
//                result = EncodingUtils.getString(buffer, "UTF-8");
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return result;
//        }
//        return "";
//    }

    /**
     * 获取外部的sdcard目录地址 Environment.getExternalStorageDirectory() = /mnt/sdcard
     *
     * @return
     */
    public static String getExternalStorageDirPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    // /**
    // * 获取程序外部的缓存目录地址 context.getExternalCacheDir() =
    // /mnt/sdcard/Android/data/com.mt.mtpp/cache
    // *
    // * @return
    // */
    // public static String getExternalCacheDirPath() {
    // return
    // MultimediaApp.getInstance().getApplicationContext().getExternalCacheDir()
    // .getAbsolutePath();
    // }
    //
    // /**
    // * 获取程序内部缓存的目录地址 context.getCacheDir() = /data/data/com.mt.mtpp/cache
    // *
    // * @return
    // */
    // public static String getCacheDirPath() {
    // return
    // MultimediaApp.getInstance().getApplicationContext().getCacheDir().getAbsolutePath();
    // }

    // /**
    // * 获取程序内部files目录地址 context.getFilesDir() = /data/data/com.mt.mtpp/files
    // *
    // * @return
    // */
    // public static String getFilesDirPath() {
    // return
    // MultimediaApp.getInstance().getApplicationContext().getFilesDir().getAbsolutePath();
    // }

    @SuppressWarnings("unused")
    private static File getStorageFolder(Context ctxt, boolean preferCache) {
        File root = Environment.getExternalStorageDirectory();
        if (!root.canWrite() || preferCache) {
            root = ctxt.getCacheDir();
        }

        if (root.canWrite()) {
            // TODO 文件夹将该为定义好的值
            File dir = new File(root.getAbsolutePath() + File.separator + "AllKids");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }
        return null;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     *
     * @return The external cache dir
     */
    public static File getExternalCacheDir(final Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();

        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     *
     * @return The external cache dir
     */
    public static File getSaveCacheDir(final Context context) {
        File f;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
                f = context.getExternalCacheDir();
                if (null == f) {
                    return context.getCacheDir();
                } else {
                    return f;
                }
            } else {
                return context.getCacheDir();

            }
        } catch (Exception e) {
            return context.getCacheDir();
        }
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * 删除文件夹
     *
     * @param file
     *
     * @return 返回是否成功
     */
    public static boolean deleteFolder(File file) {
        boolean isSuccess = true;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                isSuccess = file.delete();
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                if (files != null) {
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        if (!deleteFolder(files[i])) // 把每个文件 用这个方法进行迭代
                            isSuccess = false;
                    }
                }
                if (!file.delete())// 删除目录
                    isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * 删除文件夹下的更新文件
     *
     * @param file
     *
     * @return 返回是否成功
     */
    public static boolean deleteUpdateFolder(File file) {
        boolean isSuccess = true;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                isSuccess = file.delete();
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        // TODO Auto-generated method stub
                        return filename.toLowerCase().endsWith(".update");
                    }
                }); // 声明目录下所有的文件 files[];
                if (files != null) {
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        if (!deleteUpdateFolder(files[i])) // 把每个文件 用这个方法进行迭代
                            isSuccess = false;
                    }
                }
                if (!file.delete())// 删除目录
                    isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * 创建固定大小文件
     *
     * @param file
     *
     * @return 返回是否成功
     */
    public static void createFile(File file, int size) {
        long start = System.currentTimeMillis();
        FileOutputStream fos = null;
        FileChannel output = null;
        try {
            fos = new FileOutputStream(file);
            output = fos.getChannel();
            output.write(ByteBuffer.allocate(1), size - 1);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        // Debug.print("total times " + (end - start));
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     *
     * @return
     */
    public static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS == 0) {
            fileSizeString = "0B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 取得耽搁文件的大小
     *
     * @param f
     *
     * @return
     *
     * @throws Exception
     */
    public static long getSingleFileSize(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        }
        // 服务器对文件进行了转化，详情页返回的课件大小的值不准确
        // 首先对文件大小除以1024，取小数点后一位，不四舍五入。
        // 然后再将该值乘以1024，舍小数点后的书中，不四舍五入
        double a = s / 1024.0;
        BigDecimal bg = new BigDecimal(a);
        double f1 = bg.setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
        s = (long) (f1 * 1024);
        return s;
    }

    /**
     * 取得文件夹中大小
     *
     * @param f
     *
     * @return
     *
     * @throws Exception
     */
    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static int copy(String fromFile, String toFile) {
        // 要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        // 如同判断SD卡是否存在或者文件是否存在
        // 如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        // 如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        // 目标目录
        File targetDir = new File(toFile);
        // 创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        // 遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())// 如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else// 如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }

    //
    // 文件拷贝
    // 要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static int CopySdcardFile(String fromFile, String toFile) {

        try {
            File targetDir = new File(toFile);
            if (!targetDir.exists()) {
                targetDir.createNewFile();
            }
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

    }

    public static int CopySdcardFileDecrypt(String fromFile, String toFile) {

        // File path = new File(Constants.COURSEALBUM_SAVEPATH + File.separator
        // + "TEMP");
        // if (!path.exists()) {
        // try {
        // boolean isSuccess = path.mkdirs();
        // if (!isSuccess) {
        // }
        // } catch (Exception e) {
        // }
        // }
        // File mainFile = new File(toFile);
        //
        // if (mainFile.exists()) {
        // mainFile.delete();
        // }
        // try {
        // boolean bo = mainFile.createNewFile();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        File mainFile = new File(toFile);
        if (mainFile.exists()) {
            mainFile.delete();
        }
        try {
            mainFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[102400];
            int c;
            int i = 0;
            byte[] encrypt = new byte[128];
            while ((c = fosfrom.read(bt)) > 0) {

                // 2.对临时文件进行解密
                if (i == 0) {
                    for (int j = 0; j < 128; j++) {
                        encrypt[j] = bt[j];
                    }
                    Log.d("encrypt2 string:",Arrays.toString(encrypt));
                    byte[] original = AESUtil.decrypt(encrypt, "12345678");
                    Log.d("original2 string:",Arrays.toString(original));
                    for (int j = 0; j < 128; j++) {
                        bt[j] = original[j];
                    }
                }

                fosto.write(bt, 0, c);
                i++;
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取扩展存储路径，TF卡、U盘
     */
    public static String getExternalStorageDirectory() {
        String dir = new String();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        dir = dir.concat(columns[1] + "\n");
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        dir = dir.concat(columns[1] + "\n");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
    }

    public static String getPathFromName(String name) {
        String result = null;
        int index = name.lastIndexOf("/");
        if (index == -1)
            result = null;
        else {
            result = name.substring(0, index + 1);
        }
        if (result != null) {
            File path = new File(result);
            if (!path.exists()) {
                try {
                    path.mkdirs();
                } catch (Exception e) {

                }
            }
        }
        return result;
    }

    /**
     * @return true if exsists and can write, false on others
     *
     * @description check the ExternalStorage status
     */
    public static boolean checkExternalStorageStatus() {
        boolean mExternalStorageAvailable, mExternalStorageWriteable;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    public static File newCacheImagePath(String src) throws IOException, BackingStoreException {
        return newFile(WORK_DIRECTORY_CACHE_IMAGE, identify(src) + "");
    }

    public static File newCaptureImagePath() throws IOException, BackingStoreException {
        return newFile(WORK_DIRECTORY_CAPTURE, System.currentTimeMillis() + ".jpg");
    }

    public static File newImageCacheFile() throws IOException, BackingStoreException {
        return newFile(WORK_DIRECTORY_CACHE_IMAGE, System.currentTimeMillis() + ".jpg");
    }

    public static File newImageCacheFile(String path) throws IOException, BackingStoreException {
        return newFile(WORK_DIRECTORY_CACHE_IMAGE, identify(path) + "");
    }

    public static File newDownloadCacheFile(String fileName) throws IOException, BackingStoreException {
        return newFile(WORK_DIRECTORY_DOWNLOAD, fileName);
    }

    public static boolean exsistsDownloadCacheFile(String fileName) throws IOException, BackingStoreException {
        return exsistsFile(WORK_DIRECTORY_DOWNLOAD + "fileName");
    }

    public static File newVideoPath(String fileName) throws IOException, BackingStoreException {
        return newFile(WORK_DIRECTORY_VIDEO, "" + fileName + ".mp4");
    }

    public static boolean exsistsFile(String path) throws IOException, BackingStoreException {
        if (!checkExternalStorageStatus()) {
            throw new BackingStoreException("external storage not found");
        }
        File file = new File(path);
        return file.exists();
    }

    public static int identify(Object obj) {
        return obj.hashCode();
    }

    public static synchronized File newFile(String workDir, String fileName) throws IOException,
            BackingStoreException {
        if (!checkExternalStorageStatus()) {
            throw new BackingStoreException("external storage not found");
        }
        File wd = new File(workDir);
        if (!wd.exists())
            wd.mkdirs();
        File file = new File(wd, fileName);
        if (!file.exists())
            file.createNewFile();
        return file;
    }

    public static final boolean installApk(Context context, String fileName) {
        File apk = new File(fileName);
        return installApk(context, apk);
    }

    public static final boolean installApk(Context context, File file) {
        boolean result = false;
        if (file.exists() && file.isFile()) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            if (BuildUtil.hasNougat()){
                intent.setDataAndType(PictureUtils.getUriFromFile(context,file), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            result = true;
        } else {
//            Toast.makeText(context, R.string.no_apk, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}
