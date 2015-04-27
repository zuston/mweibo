package com.example.mweibo.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.mweibo.R;
import com.example.mweibo.animation.ZoomOutPageTransformer;
import com.example.mweibo.utils.WBLogin;



public class Main_1_Fragment extends Fragment{


	
private ViewPager viewPager;
private List<Fragment> list;
//private FragmentPagerAdapter fragmentPagerAdapter;
private FragmentStatePagerAdapter fragmentPagerAdapter;

//private View view;
private int positonItem;
private FontAwesomeText tab1AwesomeText;
private FontAwesomeText tab2AwesomeText;
private FontAwesomeText tab3AwesomeText;
private ImageView pointerImageView;
private LayoutParams  lp;
private int width;
private TextView hostnameTextView;

public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	 View view= inflater.inflate(R.layout.fragment_main_tab_1, container, false);
	 
	 
	 
	 init(view);
	 initPointer(view);
	
	viewPager.setOffscreenPageLimit(3);
	viewPager.setOnPageChangeListener(new listener());
//	viewPager.setCurrentItem(0);
	
	
	
	
	return view;
	
	
	
}

private void initPointer(View view) {
	
	Resources r=this.getResources();
	//配合cursor的长度，转化为pixel
	width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, r.getDisplayMetrics());
	pointerImageView=(ImageView)view.findViewById(R.id.pointer);
	LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) pointerImageView.getLayoutParams();
	pointerImageView.setLayoutParams(lp);

}

@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
class listener implements OnPageChangeListener{

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPx) {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp =   (android.widget.LinearLayout.LayoutParams) pointerImageView
				.getLayoutParams();
		
		reset();
		positonItem=viewPager.getCurrentItem();
		if (positonItem == 0 && position == 0)// 0->1
		{	
			
			lp.leftMargin = (int) (positionOffset * width + positonItem
					* width);
		} else if (positonItem == 1 && position == 0)// 1->0
		{
			lp.leftMargin = (int) (positonItem * width + (positionOffset - 1)
				* width);
	} else if (positonItem == 1 && position == 1) // 1->2
		{
			lp.leftMargin = (int) (positonItem * width + positionOffset
					* width);
		} else if (positonItem == 2 && position == 1) // 2->1
		{
			lp.leftMargin = (int) (positonItem * width + ( positionOffset-1)
					* width);
		}
		pointerImageView.setLayoutParams(lp);
		setColor(positonItem);
	}

	
	private void setColor(int positonItem) {
		switch (positonItem) {
		case 0:
			tab1AwesomeText.setTextColor(Color.parseColor("#BA55D3"));;
			break;
		case 1:
			tab2AwesomeText.setTextColor(Color.parseColor("#BA55D3"));
			break;
		case 2:
			tab3AwesomeText.setTextColor(Color.parseColor("#BA55D3"));
			break;
		default:
			break;
		}
		
	}

	private void reset() {
		// TODO Auto-generated method stub
		tab1AwesomeText.setTextColor(Color.parseColor("#FFFFFF"));
		tab2AwesomeText.setTextColor(Color.parseColor("#FFFFFF"));
		tab3AwesomeText.setTextColor(Color.parseColor("#FFFFFF"));
	}

	

	@Override
	public void onPageSelected(int arg0) {
//		positonItem=viewPager.getCurrentItem();
//		set(arg0);
		
	}
	
}
private void init(View view) {
	tab1AwesomeText=(FontAwesomeText) view.findViewById(R.id.tab1);
	tab2AwesomeText=(FontAwesomeText) view.findViewById(R.id.tab2);
	tab3AwesomeText=(FontAwesomeText) view.findViewById(R.id.tab3);
	
	
	
	viewPager=(ViewPager) view.findViewById(R.id.viewpager);
	
	list=new ArrayList<Fragment>();
	Main_1_1_Fragment    f1=new Main_1_1_Fragment();

	Main_1_2_Fragment    f2=new Main_1_2_Fragment();

	Main_1_3_Fragment 	 f3=new Main_1_3_Fragment();
	list.add(f1);
	list.add(f2);
	list.add(f3);
	
	

	
	fragmentPagerAdapter=new FragmentStatePagerAdapter(getFragmentManager()) {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}
	};
	
	
	
	
	viewPager.setAdapter(fragmentPagerAdapter);
	viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
	
	
	
}

protected void set(int arg0) {
	// TODO Auto-generated method stub
	
	viewPager.setCurrentItem(arg0);
}


}
