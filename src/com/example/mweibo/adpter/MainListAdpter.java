package com.example.mweibo.adpter;

import java.util.List;
import java.util.Map;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.mweibo.R;


import com.example.mweibo.activity.ContentActivity;
import com.example.mweibo.activity.ReplyActivity;
import com.example.mweibo.utils.LoadImage;
import com.pkmmte.circularimageview.CircularImageView;
import com.sina.weibo.sdk.R.string;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainListAdpter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private List<Map<String, Object>> listItems;
	
	private String commentUsernameString;
	private String commentInfoString;
	
	public MainListAdpter(Context context,List<Map<String, Object>> listItem){
		this.inflater=LayoutInflater.from(context);
		this.listItems=listItem;
		this.context=context;
	}
	
	//获取列表项的数量
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override                                                                                                                                                                                                                                                                                                                                                                                 
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Map<String, Object> mdataMap=listItems.get(position);
		return mdataMap;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.v("Myadpter", "位置为:"+position+"视图为"+convertView);
		viewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.fragment_main_tab_1_1_list, null);
			viewHolder=new viewHolder();
			
			viewHolder.usernameTextView=(TextView) convertView.findViewById(R.id.usernameTextview);
			viewHolder.contentTextView=(TextView) convertView.findViewById(R.id.contentTextview);
			viewHolder.createtimeTextView=(TextView) convertView.findViewById(R.id.createTimeTextview);
			viewHolder.headPic=(CircularImageView) convertView.findViewById(R.id.headPic);
			viewHolder.commentNumberTextView=(TextView) convertView.findViewById(R.id.commentTextview);
			viewHolder.repostNumberTextView=(TextView) convertView.findViewById(R.id.reportTextview);
			viewHolder.oldIdTextView=(TextView) convertView.findViewById(R.id.oldIdTextview);
			viewHolder.oldInfoTextView=(TextView) convertView.findViewById(R.id.oldInfoTextview);
			viewHolder.contentImageView1=(ImageView) convertView.findViewById(R.id.contentPic0);
			viewHolder.commentAwesomeText=(FontAwesomeText) convertView.findViewById(R.id.commentTextviewButton);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(com.example.mweibo.adpter.MainListAdpter.viewHolder) convertView.getTag();
		}
		viewHolder.usernameTextView.setText(listItems.get(position).get("username").toString());
		viewHolder.contentTextView.setText(listItems.get(position).get("content").toString());
		viewHolder.createtimeTextView.setText(listItems.get(position).get("time").toString());
		viewHolder.commentNumberTextView.setText(listItems.get(position).get("commentsNum")+"");
		viewHolder.repostNumberTextView.setText(listItems.get(position).get("repostNum")+"");
		if(listItems.get(position).get("headerImage")!=null){
			new LoadImage().execute(viewHolder.headPic,listItems.get(position).get("headerImage"));
		}else{
			
		}
		if(listItems.get(position).get("contentPic1")!=null){
			new LoadImage().execute(viewHolder.contentImageView1,listItems.get(position).get("contentPic1"));
		}else{
			viewHolder.contentImageView1.setImageBitmap(null);
		}
		if(listItems.get(position).get("oldId")!=null){
			viewHolder.oldIdTextView.setText(listItems.get(position).get("oldId").toString());
			viewHolder.oldInfoTextView.setText(listItems.get(position).get("oldInfo").toString());
		}else{
			viewHolder.oldIdTextView.setText("");
			viewHolder.oldInfoTextView.setText("");
		}
		
		//listview的评论事件的监听
		viewHolder.commentAwesomeText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commentUsernameString=listItems.get(position).get("username").toString();
				commentInfoString=listItems.get(position).get("content").toString();
				Toast.makeText(context,"评论点击:"+commentUsernameString, Toast.LENGTH_LONG).show();
				Bundle bundle=new Bundle();
				bundle.putSerializable("Wcomment", commentUsernameString);
				Intent intent=new Intent(context,ReplyActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class viewHolder{
		public TextView usernameTextView;
		public TextView createtimeTextView;
		public TextView contentTextView;
		public CircularImageView headPic;
		public TextView commentNumberTextView;
		public TextView repostNumberTextView;
		public TextView oldIdTextView;
		public TextView oldInfoTextView;
		
		public ImageView contentImageView1;
		
		public FontAwesomeText commentAwesomeText;
	}
}
