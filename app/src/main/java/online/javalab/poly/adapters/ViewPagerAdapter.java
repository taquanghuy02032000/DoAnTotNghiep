package online.javalab.poly.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> mListFragment = new ArrayList<>();
    private List<String> mTitleFragment = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addFragment(Fragment fragment, String title) {
        mListFragment.add(fragment);
        mTitleFragment.add(title);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getItemCount() {
        if (mListFragment != null)
            return mListFragment.size();
        return 0;
    }
}
