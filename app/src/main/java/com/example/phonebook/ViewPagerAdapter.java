package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.util.ArrayUtils.newArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> lstFragment = new ArrayList<>();
    private final List<String> lastTitles = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lastTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lastTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String title){
        lstFragment.add(fragment);
        lastTitles.add(title);
    }
}
