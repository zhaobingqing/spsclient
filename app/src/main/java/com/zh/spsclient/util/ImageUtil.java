package com.zh.spsclient.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
/*
 * 图片存储、转换、压缩类
 * @author Lee
 * @Date 20140404
 */
public class ImageUtil {
	public ImageUtil() {
		//构造
	}
	/*
	 * 将bitmap文件转成字符串
	 */
	public static String bitmapToString(String filePath)
	{
		Bitmap bm=getCompressionBitmap(filePath);
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		//图片压缩比，100为不压缩
		bm.compress(Bitmap.CompressFormat.JPEG,40,baos);
		byte[] b=baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	/*
	 * 根据路径获取压缩后的bitmap用于显示，拍完照后显示图片
	 * @param 文件路径
	 * @return Bitmap
	 */
	public static Bitmap getCompressionBitmap(String filePath) {
		//加载位图图片资源到内存中
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(filePath,options);
		// 计算 inSampleSize
		//显示图片的尺寸大小
		options.inSampleSize=claculateinSampleSize(options, 480, 680);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds=false;
		Bitmap arg2= BitmapFactory.decodeFile(filePath,options);
		int degree = getBitmapDegree(filePath);	
		if(0 != degree)
		{
			arg2=rotateBitmapByDegree(arg2, degree);
		}
		return arg2;
	}
	
	/**
	 * 计算图片的缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int claculateinSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
		//原始图片的设度和宽度
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		if(height>reqHeight ||width>reqWidth){
			//Calculate ratios of height and width to requested height and width
			final int heightRatio=Math.round((float)height/(float)reqHeight);
			final int widthRatio=Math.round((float)width/(float)reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
		}
		return inSampleSize;
	}
	/*
	 * 根据图片路径删除图片
	 */
	public static void  deleteTempFile(String path) {
		File file=new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	
	/*
	 * 添加图片到图库
	 */
	public static void galleryAddPic(Context context,String path){
		
		Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file=new File(path);
		Uri contentUri=Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
	    context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存图片路径
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "SmartPatrolSystem";
	}
	
	/**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) 
    {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
      
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
            bm = null;
        }
        return returnBm;
    }
}
