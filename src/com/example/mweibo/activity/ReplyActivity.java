package com.example.mweibo.activity;

import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.mweibo.R;
import com.example.mweibo.activity.MainActivity.mlistener;
import com.example.mweibo.utils.AccessTokenKeeper;
import com.example.mweibo.utils.Constants;
import com.example.mweibo.utils.SerializableMap;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReplyActivity extends Activity{
	
	private Map<String, Object> mMap;
	private Oauth2AccessToken mAccessToken;
	private CommentsAPI mCommentsAPI;
	
	private BootstrapEditText replyEditView;
	private BootstrapButton cancelButton;
	private BootstrapButton sendButton;
	
	private String weiboId2;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_reply);
		StatusSetView();
		
		Bundle bundle=getIntent().getExtras();
		if((bundle.get("data"))!=null){
			SerializableMap map=(SerializableMap) bundle.get("data");
			this.mMap=map.getMap();
			initViewComment();
		}else{
			//Toast.makeText(ReplyActivity.this, "comment", Toast.LENGTH_LONG).show();
			weiboId2=bundle.get("Wcomment").toString();
			initViewWeibo();
		}
	}
	
	//初始化对于微博的评论
	private void initViewWeibo() {
		replyEditView=(BootstrapEditText) findViewById(R.id.ReplyinfoTextview);
		cancelButton=(BootstrapButton) findViewById(R.id.ReplycancelButton);
		sendButton=(BootstrapButton) findViewById(R.id.ReplysendButton);
		replyEditView.setHint("对于微博昵称为       "+weiboId2+"   的评论");
		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(TextUtils.isEmpty(replyEditView.getText().toString())){
					Toast.makeText(ReplyActivity.this, "不能发送空微博,请输入内容", Toast.LENGTH_LONG).show();
				}else{
					mAccessToken = AccessTokenKeeper.readAccessToken(ReplyActivity.this);
				    mCommentsAPI = new CommentsAPI(ReplyActivity.this, Constants.APP_KEY, mAccessToken);
					mCommentsAPI.create(replyEditView.getText().toString(), Long.parseLong(weiboId2), true, new mlistener());
				}
			}
		});
	}

	//初始化对于微博评论的回复
	private void initViewComment() {
		replyEditView=(BootstrapEditText) findViewById(R.id.ReplyinfoTextview);
		cancelButton=(BootstrapButton) findViewById(R.id.ReplycancelButton);
		sendButton=(BootstrapButton) findViewById(R.id.ReplysendButton);
		String replyName=mMap.get("commentUsername2").toString();
		replyEditView.setHint("@"+replyName+"  评论的回复");
		sendButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(TextUtils.isEmpty(replyEditView.getText().toString())){
						Toast.makeText(ReplyActivity.this, "不能发送空微博,请输入内容", Toast.LENGTH_LONG).show();
					}else{
						final String infoString=replyEditView.getText().toString();
					    final String weiboId=mMap.get("weiboId").toString();
					    final String id=mMap.get("commentId").toString();
						mAccessToken = AccessTokenKeeper.readAccessToken(ReplyActivity.this);
					    mCommentsAPI = new CommentsAPI(ReplyActivity.this, Constants.APP_KEY, mAccessToken);
					    mCommentsAPI.reply(Long.parseLong(id), Long.parseLong(weiboId), infoString, false, true, new mlistener());
					}    
				}
			});
		    
	}
	private void StatusSetView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setTintColor(Color.parseColor("#0664C1"));
        }
		ActionBar actionBar=getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	public class mlistener implements RequestListener{
		public void onComplete(String response) {
			
//			ReplyActivity.this.finish();
			
			//Toast.makeText(ReplyActivity.this, "评论成功",Toast.LENGTH_SHORT);
			Intent intent=new Intent(ReplyActivity.this,MainActivity.class);
			startActivity(intent);
			
		} 

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			System.out.println("error");
		}
	}
}
