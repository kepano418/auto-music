package net.kepano.automusic;

import net.kepano.database.DBAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class AutoMusicActivity extends FragmentActivity {
	private static final int NUM_PAGES = 3;
	private static DBAdapter database;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DBAdapter(this);
		database.open();
		setContentView(R.layout.main1);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(1);


	}

	@Override
	protected void onDestroy() {
		database.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}





	public static DBAdapter getDB() {
		return database;
	}

	/*
	 * private void initTestCase() { Button tb = (Button)
	 * findViewById(R.id.testButton);
	 * 
	 * 
	 * //on click change choice tb.setOnClickListener(new OnClickListener() {
	 * 
	 * public void onClick(View arg0) { keyCheck kc = new keyCheck();
	 * Toast.makeText(getApplicationContext(),
	 * kc.isProInstalled(getApplicationContext()) + "",
	 * Toast.LENGTH_SHORT).show(); } }); }
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position){
			case 0:
				return new net.kepano.fragments.wireFrag();
			case 1:
				return new net.kepano.fragments.serviceFrag();
			case 2:
				return new net.kepano.fragments.bluetoothListFrag();
			}
			return null;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

}