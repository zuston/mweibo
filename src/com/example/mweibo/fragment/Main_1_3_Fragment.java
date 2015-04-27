package com.example.mweibo.fragment;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.mweibo.R;
import com.example.mweibo.utils.AccessTokenKeeper;
import com.example.mweibo.utils.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class Main_1_3_Fragment extends Fragment{
	private BootstrapEditText infoEditText;
	private BootstrapButton sendButton;
	private BootstrapButton cancelButton;
	private String infoString;
	
	private AuthInfo mAuthInfo;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	
	public resetEditViewListener mResetEditView;
	public interface resetEditViewListener{
		public void resetEditView();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_main_tab_1_3, container, false);
		return view;
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		infoEditText=(BootstrapEditText) getActivity().findViewById(R.id.infoTextview);
		sendButton=(BootstrapButton) getActivity().findViewById(R.id.sendButton);
		cancelButton=(BootstrapButton) getActivity().findViewById(R.id.cancelButton);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("click");
				if(TextUtils.isEmpty(infoEditText.getText().toString())){
					Toast.makeText(getActivity(), "不能发送空微博,请输入内容", Toast.LENGTH_LONG).show();
				}else{
					infoString=infoEditText.getText().toString();
					mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
				 	mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
				    mStatusesAPI.update(infoString, null, null, new mlistener());
				}
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mResetEditView.resetEditView();
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//实例化一个回调函数的mResetEditView
		mResetEditView=(resetEditViewListener) activity;
	}
	
	
	public class mlistener implements RequestListener{

		@Override
		public void onComplete(String arg0) {
			Toast.makeText(getActivity(), "您的微博发送成功", Toast.LENGTH_LONG).show();
			mResetEditView.resetEditView();
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "网络故障,请重试", Toast.LENGTH_LONG).show();
		}
		
	}
	
}
