package com.example.android.rescueandroidapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter extends FragmentPagerAdapter {
    public Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
            case 1:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Groups";
            case 1:
                return "Contacts";

            default:
                return null;
        }
    }
}
