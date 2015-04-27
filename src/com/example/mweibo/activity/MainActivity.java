package com.example.mweibo.activity;



import java.text.SimpleDateFormat;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.mweibo.R;
import com.example.mweibo.fragment.Main_1_1_Fragment;
import com.example.mweibo.fragment.Main_1_3_Fragment;
import com.example.mweibo.fragment.Main_1_Fragment;
import com.example.mweibo.fragment.Main_2_Fragment;
import com.example.mweibo.utils.AccessTokenKeeper;
import com.example.mweibo.utils.Constants;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.readystatesoftware.systembartint.SystemBarTintManager.SystemBarConfig;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;



import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import android.content.ClipData.Item;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements Main_1_3_Fragment.resetEditViewListener{
               
    public static final String[] TITLES = { "weibo", "知乎" };
    private DrawerLayout mDrawer_layout;//DrawerLayout容器
    private RelativeLayout mMenu_layout_left;//左边抽屉
    private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            
        mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            /*window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setTintColor(Color.parseColor("#0664C1"));
            SystemBarConfig config = tintManager.getConfig();
            mDrawer_layout.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
        }
        
        //demo
        mAuthInfo = new AuthInfo(MainActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
        
        
        mMenu_layout_left = (RelativeLayout) findViewById(R.id.menu_layout_left);
        ListView menu_listview_l = (ListView) mMenu_layout_left.findViewById(R.id.menu_listView_l);
     
                         
        menu_listview_l.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, TITLES));
      
                         
        //监听菜单
        menu_listview_l.setOnItemClickListener(new DrawerItemClickListenerLeft());
        
        initFragment();
        
        
        
    	
       
    }
    
    //demo
    
   public void test_logout(){
	   AccessTokenKeeper.clear(MainActivity.this);
       mAccessToken = new Oauth2AccessToken();
       Toast.makeText(this, "您已经退出", Toast.LENGTH_LONG).show();
   }
    
   public void test_login(){
	   mSsoHandler.authorizeWeb(new AuthListener());
   }
   public void test_refresh(){
//	     mAccessToken = AccessTokenKeeper.readAccessToken(this);
//       mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
//       mStatusesAPI.update("my app 第一条weibo", null, null, mlistener);
	     initFragment();
   }
   class AuthListener implements WeiboAuthListener{
       
       @Override
       public void onComplete(Bundle values) {
           // 从 Bundle 中解析 Token
           mAccessToken = Oauth2AccessToken.parseAccessToken(values);
           if (mAccessToken.isSessionValid()) {
               // 保存 Token 到 SharedPreferences
              AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
              Toast.makeText(MainActivity.this, 
                      "登陆成功", Toast.LENGTH_SHORT).show();
              //initFragment();
       }
       }
		@Override
       public void onCancel() {
           Toast.makeText(MainActivity.this, 
                  "cancel", Toast.LENGTH_LONG).show();
       }

       @Override
       public void onWeiboException(WeiboException e) {
           Toast.makeText(MainActivity.this, 
                   "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
       }
   }
  class mlistener implements RequestListener{

	@Override
	public void onComplete(String arg0) {
	
		Toast.makeText(MainActivity.this, "send is ok", Toast.LENGTH_LONG);
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub
		
	}
	  
  }

   

   
   
   
   
   
   
   
   
   
   
   
   
    private void initFragment() {
    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	
		Main_1_Fragment fragment=new Main_1_Fragment();
		ft.replace(R.id.fragment_layout, fragment);
		ft.addToBackStack(null);
		ft.commit();
    }
	/**
     * 左侧列表点击事件      
     * @author busy_boy
     *
     */
    public class DrawerItemClickListenerLeft implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;
                             
            //根据item点击行号判断启用哪个Fragment
            switch (position)
            {
                case 0:
                    fragment = new Main_1_Fragment();
                    break;
                case 1:
                    fragment = new Main_2_Fragment();
                    break;
                
            }
            
            
            
            ft.replace(R.id.fragment_layout,fragment);
            ft.addToBackStack(null);
            ft.commit();
            System.out.println("the repalce has been done");
            mDrawer_layout.closeDrawer(mMenu_layout_left);//关闭mMenu_layout
            }           
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id==R.id.action_login) {
			test_login();
			test_refresh();
		}else if (id==R.id.action_logout) {
			test_logout();
			test_refresh();
		}else if (id==R.id.action_refresh) {
			test_refresh();
			
		}
		return super.onOptionsItemSelected(item);
	}
	//测试方法
	public void fragmentChildOneCallback(int i){
		Intent intent=new Intent(this,ContentActivity.class);
		startActivity(intent);
	}

	@Override
	public void resetEditView() {
		// TODO Auto-generated method stub
		EditText infoEditText=(EditText) findViewById(R.id.infoTextview);
		infoEditText.setText("");
	}
	
	
	 
	 
}