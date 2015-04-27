package com.example.mweibo.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;



public class WBLogin{
	
	
	//初始化对象
	private static Oauth2AccessToken mAccessToken;
	private static SsoHandler mSsoHandler;
	private static AuthInfo mAuthInfo;
	 
	public WBLogin(){
		
		
		
	}
	
	public static void login(final Activity activity){
		 mAuthInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
	     mSsoHandler = new SsoHandler(activity, mAuthInfo);
	     mSsoHandler.authorizeWeb(new WeiboAuthListener() {
			
			public void onWeiboException(WeiboException arg0) {
				
			}
			
			public void onComplete(Bundle values) {
				 mAccessToken = Oauth2AccessToken.parseAccessToken(values);
		            if (mAccessToken.isSessionValid()) {
		                // 显示 Token
//		                updateTokenView(false);
		                // 保存 Token 到 SharedPreferences
		                AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
		                Toast.makeText(activity, "the connecting is ok", Toast.LENGTH_SHORT).show();
		            } else {
		                // 以下几种情况，您会收到 Code：
		                // 1. 当您未在平台上注册的应用程序的包名与签名时；
		                // 2. 当您注册的应用程序包名与签名不正确时；
		                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
		                String code = values.getString("code");
		                String message="the connecting has been failed";
		                if (!TextUtils.isEmpty(code)) {
		                    message = message + "\nObtained the code: " + code;
		                }
		                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
		            }
			}
			
			public void onCancel() {
				
			}
		});
		
	}
	
	protected static void updateTokenView(boolean b) {
		// TODO Auto-generated method stub
		
	}

	/**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
	
}
