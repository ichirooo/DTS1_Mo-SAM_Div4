package co.id.datascrip.mo_sam_div4_dts1.custom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        /*
		switch (index) {
		case 0:
			return new SalesHeaderFragment();
		case 1:
			return new SalesLineFragment();
		case 2:
			return new SalesTotalFragment();
		}
			
		return null;
		*/
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
