package app.touchlessChef.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.touchlessChef.constants.RecipeConstants;
import app.touchlessChef.fragment.home.ChineseFragment;
import app.touchlessChef.fragment.home.VietnamFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_COUNT = RecipeConstants.CUISINE_COUNT;

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = VietnamFragment.newInstance(RecipeConstants.VIETNAMESE);
                break;
            case 1:
                frag = ChineseFragment.newInstance(RecipeConstants.CHINESE);
                break;
        }
        assert frag != null;
        return frag;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = RecipeConstants.VIETNAMESE;
                break;
            case 1:
                title = RecipeConstants.CHINESE;
                break;
        }
        return title;
    }
}
