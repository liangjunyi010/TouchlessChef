package app.touchlessChef.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.touchlessChef.CuisineValues;
import app.touchlessChef.fragment.home.ChineseFragment;
import app.touchlessChef.fragment.home.VietnamFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_COUNT = CuisineValues.TOTAL;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = VietnamFragment.newInstance(CuisineValues.VIETNAMESE);
                break;
            case 1:
                frag = ChineseFragment.newInstance(CuisineValues.CHINESE);
                break;
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
        String title = null;
        switch (position) {
            case 0:
                title = CuisineValues.VIETNAMESE;
                break;
            case 1:
                title = CuisineValues.CHINESE;
                break;
        }
        return title;
    }
}
