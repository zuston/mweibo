package com.example.mweibo.fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.WriteAbortedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import org.apache.http.util.ByteArrayBuffer;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.example.mweibo.R;
import com.example.mweibo.R.id;
import com.example.mweibo.activity.ContentActivity;
import com.example.mweibo.adpter.MainListAdpter;
import com.example.mweibo.utils.AccessTokenKeeper;
import com.example.mweibo.utils.Constants;
import com.example.mweibo.utils.DailyTools;
import com.example.mweibo.utils.LoadImage;
import com.example.mweibo.utils.SerializableMap;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import android.R.drawable;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.ClipData.Item;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

public class Main_1_1_Fragment extends Fragment{
	
	private PullToRefreshListView listView;
	private List<Map<String, Object>> listItems;
	private SimpleAdapter simpleAdapter;
	 
	
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private int i=0;
	private int count=0;
	private String since_id;
	private int flag=0;//标示符
	
	private MainListAdpter mainListAdpter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_main_tab_1_1, container, false);
		initView(view);
		refreshView();
		return view;
	}
	
	//刷新操作
	private void refreshView() {
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new refresh().execute();
			}
      
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				
			}
		});
		
	}
	
	public class refresh extends AsyncTask<Void, Void, String>{
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(flag==1){
				mStatusesAPI.friendsTimeline(Long.parseLong(since_id), 0, 50, 1, false, 0, false, new mListener2());	
			}else{
				mStatusesAPI.friendsTimeline(0L,0L,5,1,false, 0, false, mListener);			    
			}
			return null;
		}
		protected void onPostExecute(String result){
		}
	}
	
	class mListener2 implements RequestListener{
		public void onComplete(String response) {
			if(!TextUtils.isEmpty(response)){
				 StatusList statuses = StatusList.parse(response);
                if (statuses != null && statuses.total_number > 0) {
                	List<Status> list = new ArrayList<Status>();
                	list = statuses.statusList;
                	if(list!=null){
                	for(int i=list.size()-1;i>=0;i--){
                		Status status=list.get(i);
                		Map<String, Object> item=new HashMap<String, Object>();
            			if(status.retweeted_status != null){
            				item.put("oldId", status.retweeted_status.user.screen_name+":");    
                			item.put("oldInfo", status.retweeted_status.text);
                			item.put("oldPic", status.retweeted_status.bmiddle_pic);
            			}
            			if(i==0){
            				since_id=status.id;
            			}
            			item.put("id", status.id);
            			item.put("username", status.user.screen_name);
            			item.put("time",status.created_at);
            			item.put("content", status.text);
            			item.put("comments", "评论数："+status.comments_count);
            			item.put("repost", "转发数："+status.reposts_count);
            			item.put("headerImage",status.user.profile_image_url);
            			listItems.add(0,item);
                	}
                    
                	}else{
                		Toast.makeText(getActivity(), "没有新微博", Toast.LENGTH_LONG).show();
                	}
                }else{
                	Toast.makeText(getActivity(), "没有新微博", Toast.LENGTH_LONG).show();
                }
			}else{
            	Toast.makeText(getActivity(), "信息为空", Toast.LENGTH_LONG).show();
            }                                                                           
			mainListAdpter.notifyDataSetChanged();
            listView.onRefreshComplete();
		}
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initView(View view) {
		// TODO Auto-generated method stub
		listView= (PullToRefreshListView) view.findViewById(R.id.contentList);
		listItems=new ArrayList<Map<String,Object>>();
		mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
	    mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
	    mStatusesAPI.friendsTimeline(0L,0L,20,1,false, 0, false, mListener);
	     
	    //test the mweibo.txt
	    /* 
	     * try {
			String response=new DailyTools().read("mweibo.txt",getActivity());
			simpleAdapterData(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		*/	
	}
	
	private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                	try {
                		new DailyTools().write(getActivity(), "mweibo.txt", response);
					} catch (IOException e) {
						e.printStackTrace();
					}
                	simpleAdapterData(response);
                        }
                    } 
            }
		@Override
        public void onWeiboException(WeiboException e) { 
        }
    };
    
    private void simpleAdapterData(String response){
    	//创建一个list填充status的对象
    	List<Status> list = new ArrayList<Status>();
    	//response为请求返回json数据字符串，根据statuslist这个类来解析,生成一个statuslist的statuses的对象
        StatusList statuses = StatusList.parse(response);
        //判断是否为空
        if (statuses != null && statuses.total_number > 0) {
        	flag=1;
        	//一条微博的解析在status中解析
        	list = statuses.statusList;
            for(Status status : list){
        			Map<String, Object> item=new HashMap<String, Object>();
        			if(status.retweeted_status != null){
        			    item.put("oldId", status.retweeted_status.user.screen_name+":"); 
            			item.put("oldInfo", status.retweeted_status.text);
            			item.put("oldPic", status.retweeted_status.bmiddle_pic);
        			}
        			if(count==0){
        				since_id=status.id;
        			}
        			count++;
        			
        			//获取链接图片的数量
        			List<String> picUrls=new ArrayList<String>();
        			if(status.pic_urls!=null){
        				picUrls=status.pic_urls;
        				for(int i=0;i<picUrls.size();i++){
        					System.out.println(status.user.screen_name+"  的配图地址"+picUrls.get(i));
        					item.put("contentPic"+i, picUrls.get(i));
        				}
        			}
        			item.put("id", status.id);
        			item.put("username", status.user.screen_name);
        			item.put("time",status.created_at);
        			item.put("content", status.text);
        			item.put("commentsNum", "评论数："+status.comments_count);
        			item.put("repostNum", "转发数："+status.reposts_count);
        			item.put("headerImage",status.user.avatar_large);
        			listItems.add(item);
            		}
            
            mainListAdpter=new MainListAdpter(getActivity(), listItems);
            listView.setAdapter(mainListAdpter);
            		
//        				simpleAdapter=new SimpleAdapter(getActivity(), 
//        												listItems, 
//        												R.layout.fragment_main_tab_1_1_list, 
//								        				new String[]{"headerImage",
//								        							 "username",
//								        							 "time",
//								        							 "content",
//								        							 "contentPic0",
////								        							 "contentPic1",
////								        							 "contentPic2",
////								        							 "contentPic3",
//								        							 "commentsNum",
//								        							 "repostNum",
//								        							 "oldId",
//								        							 "oldInfo",
//								        							 "oldPic",
//								        							 
//								        							 }, 
//								        				new int[]
//								        						{R.id.headPic,
//								        						 R.id.usernameTextview,
//								        						 R.id.createTimeTextview,
//								        						 R.id.contentTextview,
//								        						 R.id.contentPic0,
////								        						 R.id.contentPic1,
////								        						 R.id.contentPic2,
////								        						 R.id.contentPic3,
//								        						 R.id.commentTextview,
//								        						 R.id.reportTextview,
//								        						 R.id.oldIdTextview,
//								        						 R.id.oldInfoTextview,
//								        						 R.id.oldPic
//								        						 
//								        						 }
//        												){
//        					//重写simpleAdpter里面的图片展示方法
//        					public void setViewImage(final ImageView image,final String url){
//        						switch (image.getId()) {
//								case R.id.headPic:
//									image.setTag(url);//唯一标识
//									new LoadImage().execute(image,url);
//									break;
//								case R.id.contentPic0:
//									new LoadImage().execute(image,url);
//									break;
//								case R.id.contentPic1:
//									new LoadImage().execute(image,url);
//									break;
//								case R.id.contentPic2:
//									new LoadImage().execute(image,url);
//									break;
//								case R.id.contentPic3:
//									new LoadImage().execute(image,url);
//									break;
//								default:
//									break;
//								}
//        					}
//        				};
//        				
//        				listView.setAdapter(simpleAdapter);
        }
        listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				//继承了baseAdapter的listview重写getItem，
				Map<String, Object> map=(Map<String, Object>) parent.getItemAtPosition(position);
				Bundle bundle=new Bundle();
				SerializableMap smap=new SerializableMap();
				smap.setMap(map);
				bundle.putSerializable("itemData",smap);
				Intent intent=new Intent(getActivity(),ContentActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
    }
   
}
