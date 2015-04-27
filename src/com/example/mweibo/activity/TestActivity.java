package com.example.mweibo.activity;


import java.io.InputStream;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.example.mweibo.R;
import com.example.mweibo.utils.LoadImage;

public class TestActivity extends Activity{
	
	
	private ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		image=(ImageView) findViewById(R.id.imageTest);
		new LoadImage().execute(image,"");
	}
	
}
