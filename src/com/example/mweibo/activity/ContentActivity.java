package com.example.mweibo.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.SM;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.mweibo.R;
import com.example.mweibo.R.id;
import com.example.mweibo.activity.MainActivity.mlistener;
import com.example.mweibo.fragment.Main_1_1_Fragment.refresh;
import com.example.mweibo.utils.AccessTokenKeeper;
import com.example.mweibo.utils.Constants;
import com.example.mweibo.utils.LoadImage;
import com.example.mweibo.utils.SerializableMap;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends Activity{

	private Map<String, Object> dataMap;
	private String username;
	private String time;
	private String content;
	private String comments;
	private String repost;
	private String oldname;
	private String oldcontent;
	private String headerImage;
	private String id;
	
	private TextView usernameTextView;
	private TextView timeTextView;
	private TextView contentTextView;
	private TextView commenTextView;
	private TextView reposTextView;
	private TextView oldnameTextView;
	private TextView oldcontentTextView;
	private ImageView headerImageView;
	private FontAwesomeText commenTextButton;
	
	private PullToRefreshListView commentListView;
	private List<Map<String, Object>> listItems;
	private SimpleAdapter simpleAdapter;
	private Oauth2AccessToken mAccessToken;
	private CommentsAPI mCommentsAPI;
	private String since_id;
	private int count=0;
	private int flag=0;//标识符，如果首次加载有评论为1，无评论为0（default）
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		StatusSetView();
		initView();
		initCommentListView();
		
		
	}
	
	
	private void initCommentListView() {
		commentListView=(PullToRefreshListView) findViewById(R.id.commentListView);
		listItems=new ArrayList<Map<String,Object>>();
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
	    mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, mAccessToken);
	    mCommentsAPI.show(Long.parseLong(id), 0, 0, 50, 1, 0, new commentListener());
	    //pull to refresh listener
	    commentListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//Toast.makeText(ContentActivity.this, "pull", Toast.LENGTH_LONG).show();
				new refresh().execute();
			}
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {	
			}
		});
	    commentListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView adapter=(ListView) parent;
				Map<String, Object> map=(Map<String, Object>) adapter.getItemAtPosition(position);
				Bundle bundle=new Bundle();
				SerializableMap sMap=new SerializableMap();
				sMap.setMap(map);
				bundle.putSerializable("data", sMap);
				Intent intent=new Intent(ContentActivity.this,ReplyActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	
	public class refresh extends AsyncTask<Object, Object, String>{
		protected String doInBackground(Object... params) {
			if(flag==1){
				mCommentsAPI.show(Long.parseLong(id), Long.parseLong(since_id), 0, 50, 1, 0, new commentListener2());
				
			}else{
				 mCommentsAPI.show(Long.parseLong(id), 0, 0, 50, 1, 0, new commentListener());
			}	
			return null;
		}
	}
	public class commentListener2 implements RequestListener{
		public void onComplete(String response) {
			if(!TextUtils.isEmpty(response)){
				CommentList comments=CommentList.parse(response);
				if(comments!=null&&comments.total_number>0){
					List<Comment> list=new ArrayList<Comment>();
					list=comments.commentList;
					if(list!=null){
						for(int i=list.size()-1;i>=0;i--){
							Comment comment=list.get(i);
							Map<String, Object> itemMap=new HashMap<String, Object>();
							itemMap.put("commentUsername", "From:  "+comment.user.screen_name+"   ");
							itemMap.put("commentUsername2", comment.user.screen_name);//只是用于传递数据
							itemMap.put("commentHeaderPic", comment.user.profile_image_url);
							itemMap.put("commentContent", comment.text);
							itemMap.put("conmmentTime", comment.created_at);
							itemMap.put("commentId", comment.id);
							itemMap.put("weiboId", id);
							listItems.add(0,itemMap);
							if(i==0){
								since_id=comment.id;
							}
						}
					}
				}else{
					Toast.makeText(ContentActivity.this, "none of the new weibo comments", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(ContentActivity.this, " new weibo comment listener is null", Toast.LENGTH_LONG).show();
			}
			simpleAdapter.notifyDataSetChanged();
            commentListView.onRefreshComplete();
		}
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public class commentListener implements RequestListener{
		public void onComplete(String response) {
			if(!TextUtils.isEmpty(response)){
				CommentList comments=CommentList.parse(response);
				if(comments!=null&&comments.total_number>0){
					flag=1;
					System.out.println("flag="+flag);
					List<Comment> list=new ArrayList<Comment>();
					list=comments.commentList;
					for(Comment comment:list){
						Map<String, Object> itemMap=new HashMap<String, Object>();
						itemMap.put("commentUsername", "From:  "+comment.user.screen_name+"   ");
						itemMap.put("commentUsername2", comment.user.screen_name);//只是用于传递数据
						itemMap.put("commentHeaderPic", comment.user.profile_image_url);
						itemMap.put("commentContent", comment.text);
						itemMap.put("conmmentTime", comment.created_at);
						itemMap.put("commentId", comment.id);
						itemMap.put("weiboId", id);
						listItems.add(itemMap);
						if(count==0){
							since_id=comment.id;
						}
						count++;
					}
					simpleAdapter=new SimpleAdapter(ContentActivity.this, 
													listItems,
													R.layout.content_comment_list,
													new String[]{"commentUsername",
																 "commentHeaderPic",
																 "commentContent",
																 "conmmentTime"}, 
													new int[]{
																R.id.commentUsername,
																R.id.commentHeaderPic,
																R.id.commentContent,
																R.id.commentTime
														     }){
						public void setViewImage(final ImageView image,final String url){
    						switch (image.getId()) {
							case R.id.commentHeaderPic:
								image.setTag(url);//唯一标识
								new LoadImage().execute(image,url);
								break;
							
							default:
								break;
							}
    					}
					};
				}
				commentListView.setAdapter(simpleAdapter);
			}else{
				Toast.makeText(ContentActivity.this,"评论内容为空", Toast.LENGTH_LONG).show();
			}
		}
		public void onWeiboException(WeiboException arg0) {
		}
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
	private void initView() {
		Bundle bundle=getIntent().getExtras();
		SerializableMap map=(SerializableMap) bundle.get("itemData");
		dataMap=map.getMap();
		
		username=dataMap.get("username").toString();
		time=dataMap.get("time").toString();
		content=dataMap.get("content").toString();
		comments=dataMap.get("commentsNum").toString()+"";
		repost=dataMap.get("repostNum").toString()+"";
		headerImage=dataMap.get("headerImage").toString();
		id=dataMap.get("id").toString();
		
		usernameTextView=(TextView) findViewById(R.id.usernameTextview);
		timeTextView=(TextView) findViewById(R.id.createTimeTextview);
		contentTextView=(TextView) findViewById(R.id.contentTextview);
		commenTextView=(TextView) findViewById(R.id.commentTextview);
		reposTextView=(TextView) findViewById(R.id.reportTextview);
		headerImageView=(ImageView) findViewById(R.id.headPic);
		
		usernameTextView.setText(username);
		timeTextView.setText(time);
		contentTextView.setText(content);
		commenTextView.setText(comments);
		reposTextView.setText(repost);
		new LoadImage().execute(headerImageView,headerImage);//异步加载头像
		
		//判断是否为转发的内容
		if(dataMap.get("oldId")!=null){
			oldname=dataMap.get("oldId").toString();
			oldcontent=dataMap.get("oldInfo").toString();
			oldnameTextView=(TextView) findViewById(R.id.oldIdTextview);
			oldcontentTextView=(TextView) findViewById(R.id.oldInfoTextview);
			oldnameTextView.setText(oldname);
			oldcontentTextView.setText(oldcontent);
		}
		
		//监听是否为评论微博
		commenTextButton=(FontAwesomeText) findViewById(R.id.commentTextviewButton);
		commenTextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Toast.makeText(ContentActivity.this, "clicked", Toast.LENGTH_LONG).show();
				Bundle bundle=new Bundle();
				bundle.putSerializable("Wcomment", username);
				Intent intent=new Intent(ContentActivity.this,ReplyActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
