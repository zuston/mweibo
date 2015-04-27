package com.example.mweibo.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.example.mweibo.R;
import com.example.mweibo.activity.ContentActivity;
import com.example.mweibo.utils.AccessTokenKeeper;
import com.example.mweibo.utils.Constants;
import com.example.mweibo.utils.LoadImage;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class Main_1_2_Fragment extends Fragment{
	
	private PullToRefreshListView listView;
	private ImageView imageView;
	private List<Map<String, Object>> listItems;
	private SimpleAdapter simpleAdapter;
	private ViewPager viewPager;
	
	//demo
	private BootstrapCircleThumbnail headpiCircleThumbnail;
	private TextView commenTextView;
	private TextView reportTextView;
	private TextView usernameTextView;
	private TextView createtimeTextView;
	private TextView contentTextView;
	
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_main_tab_1_1, container, false);
		initView(view);
		return view;
	}
	private void initView(View view) {
		// TODO Auto-generated method stub
		listView= (PullToRefreshListView) view.findViewById(R.id.contentList);
		listItems=new ArrayList<Map<String,Object>>();
		 mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
	     mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
	     mStatusesAPI.mentions(0L, 0L, 15, 1, 0, 0, 0, false, mListener);

		
	}
	
	private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                	List<Status> list = new ArrayList<Status>();
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                    	list = statuses.statusList;
                        for(Status status : list){
                    			Map<String, Object> item=new HashMap<String, Object>();
                    			if(status.retweeted_status != null){
                    				item.put("oldId", status.retweeted_status.user.screen_name+":");    
                        			item.put("oldInfo", status.retweeted_status.text);
                    			}
                    			item.put("headerPic", status.user.profile_image_url);
                    			item.put("username", status.user.screen_name);
                    			item.put("time",status.created_at);
                    			item.put("content", status.text);
                    			item.put("comments", "评论数："+status.comments_count);
                    			item.put("report", "转发数："+status.reposts_count);
                    			listItems.add(item);
                    			System.out.println(status.user.profile_image_url);
                        }
                        simpleAdapter=new SimpleAdapter(getActivity(), 
                        								listItems, 
                        								R.layout.fragment_main_tab_1_1_list, 
                        								new String[]{"headerPic",
                        											 "username",
                        											 "time",
                        											 "content",
                        											 "comments",
                        											 "report",
                        											 "oldId",
                        											 "oldInfo"}, 
                        								new int[]{R.id.headPic,
                        										  R.id.usernameTextview,
                        										  R.id.createTimeTextview,
                        										  R.id.contentTextview,
                        										  R.id.commentTextview,
                        										  R.id.reportTextview,
                        										  R.id.oldIdTextview,
                        										  R.id.oldInfoTextview
                        										  }
                        								){
                        	public void setViewImage(ImageView image,String url){
                        		if(image.getId()==R.id.headPic){
                        			new LoadImage().execute(image,url);
                        		}
                        	}
                        };
                    		
                    		listView.setAdapter(simpleAdapter);
                    		listView.setOnItemClickListener(new OnItemClickListener() {

                    			@Override
                    			public void onItemClick(AdapterView<?> parent, View view,
                    					int position, long id) {
                    				
                    				Toast.makeText(getActivity(), "点击位置：  "+position, Toast.LENGTH_SHORT).show();
                    			}
                    		});
                } 
            }
        }
        public void onWeiboException(WeiboException e) {
        }
    };
	
	
    
}
