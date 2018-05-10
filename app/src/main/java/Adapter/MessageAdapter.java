package Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 赖志林 on 2018/4/17.
 */

public class MessageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = null;
    private String[] tabTitles = null;

    public MessageAdapter(FragmentManager fm, List<Fragment> fragments , String[] arrTabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = arrTabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
