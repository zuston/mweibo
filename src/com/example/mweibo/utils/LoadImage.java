package com.example.mweibo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;


import java.net.URL;
import java.net.URLEncoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

public class LoadImage extends AsyncTask<Object, Object, Bitmap>{

	private ImageView imageView;
	private Bitmap bmp=null;
	private String url=null;
	protected Bitmap doInBackground(Object... params) {
		this.imageView=(ImageView) params[0];
		this.url=(String) params[1];
		File file = null;
		try {
			file = new File(Environment.getExternalStorageDirectory()+"/cache", URLEncoder.encode((String)params[1],"UTF-8")+".txt");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//父目录的bug已经记录在lofter上，有问题到zuston.lofter.com
		if (!file.getParentFile().exists()) {  
	           if (!file.getParentFile().mkdirs()) {  
	           }  
	     }  
		if(file.exists()){
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				bmp=BitmapFactory.decodeStream(fis);
				System.out.println("from the sd");
				return bmp;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return bmp;
		}else{
			try {
				file.createNewFile();//创建文件，如果不存在的话
				bmp=BitmapFactory.decodeStream(new URL((String) params[1]).openStream());
				FileOutputStream fos=new FileOutputStream(file);
				bmp.compress(CompressFormat.JPEG, 100, fos );
				System.out.println("from the net");
				return bmp;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	protected void onPostExecute(Bitmap bmp){
		if(bmp!=null){
			imageView.setImageBitmap(bmp);
		}
	}
}