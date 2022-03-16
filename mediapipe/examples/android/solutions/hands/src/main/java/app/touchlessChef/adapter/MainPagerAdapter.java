package app.touchlessChef.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.touchlessChef.template.fragment.VietnamFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_COUNT = 1;
    private String[] tabTitles = {"Vietnam"};

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = VietnamFragment.newInstance(tabTitles[position]);
                break;
//            case 1:
//                frag = VietnamFragment.newInstance(tabTitles[position]);
//                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
