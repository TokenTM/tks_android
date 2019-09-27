package com.tokentm.sdk.wallet;

import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = 1048576L;
    public static final long ONE_GB = 1073741824L;
    private static final int BUF_SIZE = 1024;
    public static final String SYSTEM_SEPARATOR = System.getProperty("file.separator");

    public FileUtils() {
    }

    public static boolean isExternalStorageAvailable() {
        String status = Environment.getExternalStorageState();
        return !status.equals("shared") && !status.equals("unmounted") && !status.equals("removed");
    }

    public static boolean isExternalStorageMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals("mounted") || status.equals("checking") || status.equals("mounted_ro");
    }

    public static boolean isExternalStorageWritable() {
        String status = Environment.getExternalStorageState();
        return status.equals("mounted") || status.equals("checking");
    }

    public static File getExternalStorageDir() {
        return isExternalStorageAvailable() ? Environment.getExternalStorageDirectory() : null;
    }

    public static long totalSpace() {
        if (isExternalStorageAvailable()) {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = 0L;
            if (VERSION.SDK_INT >= 18) {
                blockSize = stat.getBlockSizeLong();
            }

            long totalBlocks = 0L;
            if (VERSION.SDK_INT >= 18) {
                totalBlocks = stat.getBlockCountLong();
            }

            return totalBlocks * blockSize;
        } else {
            return -1L;
        }
    }

    public static long getFreeSpace() {
        if (isExternalStorageAvailable()) {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = 0L;
            if (VERSION.SDK_INT >= 18) {
                blockSize = stat.getBlockSizeLong();
            }

            long availableBlocks = 0L;
            if (VERSION.SDK_INT >= 18) {
                availableBlocks = stat.getAvailableBlocksLong();
            }

            return availableBlocks * blockSize;
        } else {
            return -1L;
        }
    }

    public static long getFolderSize(File folder) throws IllegalArgumentException {
        if (folder != null && folder.isDirectory()) {
            long folderSize = 0L;
            String[] list = folder.list();
            if (list != null && list.length > 0) {
                for(int i = 0; i < list.length; ++i) {
                    File object = new File(folder, list[i]);
                    if (object.isDirectory()) {
                        folderSize += getFolderSize(object);
                    } else if (object.isFile()) {
                        folderSize += object.length();
                    }
                }
            }

            return folderSize;
        } else {
            throw new IllegalArgumentException("Invalid   folder ");
        }
    }

    public static String byteCountToDisplaySize(long size) {
        String displaySize;
        if (size / 1073741824L > 0L) {
            displaySize = size / 1073741824L + " GB";
        } else if (size / 1048576L > 0L) {
            displaySize = size / 1048576L + " MB";
        } else if (size / 1024L > 0L) {
            displaySize = size / 1024L + " KB";
        } else {
            displaySize = size + " bytes";
        }

        return displaySize;
    }

    public static String byteCountToDisplaySize(long size, int scale) {
        String displaySize;
        float d;
        if (size / 1073741824L > 0L) {
            d = (float)size / 1.07374182E9F;
            displaySize = getOffsetDecimal(d, scale) + " GB";
        } else if (size / 1048576L > 0L) {
            d = (float)size / 1048576.0F;
            displaySize = getOffsetDecimal(d, scale) + " MB";
        } else if (size / 1024L > 0L) {
            d = (float)size / 1024.0F;
            displaySize = getOffsetDecimal(d, scale) + " KB";
        } else {
            displaySize = size + " bytes";
        }

        return displaySize;
    }

    private static String getOffsetDecimal(float ft, int scale) {
        int roundingMode = 4;
        BigDecimal bd = new BigDecimal((double)ft);
        bd = bd.setScale(scale, roundingMode);
        ft = bd.floatValue();
        return "" + ft;
    }

    public static boolean isExists(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        } else {
            File file = new File(fileName);
            return file.exists();
        }
    }

    public static File createFile(String filename) {
        if (!TextUtils.isEmpty(filename) && isExternalStorageAvailable()) {
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    if (createDirectory(file.getParent())) {
                        file.createNewFile();
                    }
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }

            return file;
        } else {
            return null;
        }
    }

    public static File createFile(File file) {
        if (file != null && !TextUtils.isEmpty(file.getAbsolutePath())) {
            if (!file.exists()) {
                try {
                    if (createDirectory(file.getParent())) {
                        file.createNewFile();
                    }
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }

            return file;
        } else {
            return file;
        }
    }

    public static File createFile(File dirPath, String name) {
        return dirPath != null && name != null ? createFile(dirPath.getAbsolutePath() + SYSTEM_SEPARATOR + name) : null;
    }

    public static boolean createDirectory(String path) {
        File dir = new File(path);
        return dir.exists() && dir.isDirectory() ? true : dir.mkdirs();
    }

    public static String checkFile(String filePathName) {
        if (filePathName == null) {
            return null;
        } else {
            File file = new File(filePathName);
            if (file.exists() || file.isFile()) {
                try {
                    return file.getCanonicalPath();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }

            return null;
        }
    }

    public static boolean deleteFile(String filename) {
        return TextUtils.isEmpty(filename) ? false : deleteFile(new File(filename));
    }

    public static boolean deleteFile(File file) {
        return !file.exists() ? true : file.delete();
    }

    public static boolean delAllFileWithoutDir(String path) {
        boolean ret = false;
        File file = new File(path);
        if (!file.exists()) {
            return ret;
        } else if (!file.isDirectory()) {
            return ret;
        } else {
            String[] tempList = file.list();
            File temp = null;

            for(int i = 0; i < tempList.length; ++i) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }

                if (temp.isFile()) {
                    temp.delete();
                }

                if (temp.isDirectory()) {
                    delAllFileWithoutDir(path + "/" + tempList[i]);
                    ret = true;
                }
            }

            return ret;
        }
    }

    public static boolean delAllFile(String path) {
        boolean ret = false;
        File file = new File(path);
        if (!file.exists()) {
            return ret;
        } else if (!file.isDirectory()) {
            return ret;
        } else {
            String[] tempList = file.list();
            File temp = null;

            for(int i = 0; i < tempList.length; ++i) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }

                if (temp.isFile()) {
                    temp.delete();
                }

                if (temp.isDirectory()) {
                    delAllFile(path + "/" + tempList[i]);
                    delDirectory(path + "/" + tempList[i]);
                    ret = true;
                }
            }

            return ret;
        }
    }

    public static void delDirectory(String dirPath) {
        delAllFile(dirPath);
        deleteFile(dirPath);
    }

    public static void writeBytes(File file, byte[] bytes, boolean isAppend) {
        if (bytes != null && file != null) {
            BufferedOutputStream out = null;

            try {
                out = new BufferedOutputStream(new FileOutputStream(file, isAppend));
                out.write(bytes);
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException var12) {
                    var12.printStackTrace();
                }

            }

        }
    }

    public static boolean writeStr(File localFile, String content, boolean isAppend) {
        if (localFile != null && !localFile.isDirectory() && content != null) {
            createDirectory(localFile.getParent());
            FileWriter fw = null;

            boolean var5;
            try {
                fw = new FileWriter(localFile, isAppend);
                fw.write(content, 0, content.length());
                fw.flush();
                boolean var4 = true;
                return var4;
            } catch (IOException var15) {
                var15.printStackTrace();
                var5 = false;
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

            return var5;
        } else {
            return false;
        }
    }

    public static void writeStream(InputStream is, File file) {
        if (is != null && file != null) {
            BufferedInputStream in = null;
            BufferedOutputStream out = null;

            try {
                in = new BufferedInputStream(is);
                out = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[1024];

                int len;
                while((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }

                    is.close();
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException var13) {
                    var13.printStackTrace();
                }

            }

        }
    }

    public static byte[] readBytes(File file) {
        if (file != null) {
            FileInputStream fis = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                try {
                    fis = new FileInputStream(file);
                    int resetSize = fis.available();
                    if (resetSize <= 0) {
                        return null;
                    }

                    int bufferSize = resetSize < 1024 ? resetSize : 1024;
                    byte[] buffer = new byte[bufferSize];
                    boolean var6 = true;

                    int len;
                    while((len = fis.read(buffer, 0, buffer.length)) != -1) {
                        baos.write(buffer, 0, len);
                    }

                    byte[] var7 = baos.toByteArray();
                    return var7;
                } catch (FileNotFoundException var20) {
                    var20.printStackTrace();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }

                return null;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    }
                }

            }
        } else {
            return null;
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copyStream(is, os);
        return os.toByteArray();
    }

    public static byte[] getBytes(File file) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copyStream(new FileInputStream(file), os);
        return os.toByteArray();
    }

    public static void copyFile(File src, File desc) {
        if (src != null && desc != null) {
            InputStream is = null;
            FileOutputStream os = null;

            try {
                is = new FileInputStream(src);
                os = new FileOutputStream(desc);
                copyStream(is, os);
            } catch (FileNotFoundException var20) {
                var20.printStackTrace();
            } catch (IOException var21) {
                var21.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    }
                }

                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException var18) {
                        var18.printStackTrace();
                    }
                }

            }

        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        if (in != null && out != null) {
            int resetSize = in.available();
            int bufferSize = resetSize < 1024 ? resetSize : 1024;
            byte[] buffer = new byte[bufferSize];
            boolean var5 = true;

            int len;
            while((len = in.read(buffer, 0, bufferSize)) != -1) {
                out.write(buffer, 0, len);
            }

        }
    }

    public static void clearFile(String filename) {
        if (!TextUtils.isEmpty(filename)) {
            File file = createFile(filename);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (file.exists()) {
                file.delete();
            }

            try {
                file.createNewFile();
            } catch (IOException var4) {
                var4.printStackTrace();
            }

        }
    }

    public static boolean isMediaUri(String uri) {
        return uri.startsWith(Media.INTERNAL_CONTENT_URI.toString()) || uri.startsWith(Media.EXTERNAL_CONTENT_URI.toString()) || uri.startsWith(android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI.toString()) || uri.startsWith(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString());
    }

    public static String getMimeType(String filename) {
        String mimeType = null;
        if (filename != null) {
            if (filename.endsWith(".3gp")) {
                mimeType = "video/3gpp";
            } else if (filename.endsWith(".mid")) {
                mimeType = "audio/mid";
            } else if (filename.endsWith(".mp3")) {
                mimeType = "audio/mpeg";
            } else if (filename.endsWith(".xml")) {
                mimeType = "text/xml";
            } else {
                mimeType = "";
            }
        }

        return mimeType;
    }

    public static boolean isVideo(String filename) {
        String mimeType = getMimeType(filename);
        return mimeType != null && mimeType.startsWith("video/");
    }

    public static boolean isAudio(String filename) {
        String mimeType = getMimeType(filename);
        return mimeType != null && mimeType.startsWith("audio/");
    }

    public static String getFileCharset(String sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];

        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(3);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset;
            }

            if (first3Bytes[0] == -1 && first3Bytes[1] == -2) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == -2 && first3Bytes[1] == -1) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == -17 && first3Bytes[1] == -69 && first3Bytes[2] == -65) {
                charset = "UTF-8";
                checked = true;
            }

            bis.reset();
            if (!checked) {
                label72:
                do {
                    do {
                        if ((read = bis.read()) == -1 || read >= 240 || 128 <= read && read <= 191) {
                            break label72;
                        }

                        if (192 <= read && read <= 223) {
                            read = bis.read();
                            continue label72;
                        }
                    } while(224 > read || read > 239);

                    read = bis.read();
                    if (128 <= read && read <= 191) {
                        read = bis.read();
                        if (128 <= read && read <= 191) {
                            charset = "UTF-8";
                        }
                    }
                    break;
                } while(128 <= read && read <= 191);
            }

            bis.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return charset;
    }

    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }

        return filename;
    }

    public static String zipFile(String fileName, String zipFile) {
        if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(zipFile)) {
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(zipFile)));
                    out.putNextEntry(new ZipEntry(file.getName()));
                    out.setComment("Zip File");

                    int len;
                    while((len = inputStream.read()) != -1) {
                        out.write(len);
                    }

                    inputStream.close();
                    out.finish();
                    out.close();
                } catch (Exception var6) {
                    var6.printStackTrace();
                }

                return zipFile;
            }
        }

        return null;
    }

    public static String zipFile(File[] srcFiles, String zipFile) {
        if (srcFiles != null && !TextUtils.isEmpty(zipFile)) {
            byte[] buf = new byte[1024];

            try {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(zipFile)));

                for(int i = 0; i < srcFiles.length; ++i) {
                    FileInputStream in = new FileInputStream(srcFiles[i]);
                    out.putNextEntry(new ZipEntry(srcFiles[i].getName()));

                    int len;
                    while((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    out.closeEntry();
                    in.close();
                }

                out.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            return zipFile;
        } else {
            return null;
        }
    }

    public static void unZipFile(String zipFile, String fileName) {
        if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(zipFile)) {
            File file = new File(zipFile);
            File outFile = new File(fileName);

            try {
                ZipFile zpFile = new ZipFile(file);
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
                ZipEntry entry = zipInputStream.getNextEntry();
                InputStream input = zpFile.getInputStream(entry);
                FileOutputStream output = new FileOutputStream(outFile);

                int len;
                while((len = input.read()) != -1) {
                    output.write(len);
                }

                input.close();
                output.close();
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }

    }
}
