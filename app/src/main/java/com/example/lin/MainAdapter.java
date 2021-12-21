package com.example.lin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.LinkedList;
import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {
    private List list;

    public MainAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.list = new LinkedList();
        list.add(new MsgFragment());
        list.add(new InfoFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return (Fragment) list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
