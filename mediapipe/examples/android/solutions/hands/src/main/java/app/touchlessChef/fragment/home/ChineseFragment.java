package app.touchlessChef.fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.touchlessChef.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChineseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChineseFragment extends BaseFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_chinese;
    }
}