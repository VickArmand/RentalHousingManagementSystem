package com.example.rentalhousingmanagementsystem.ui.main;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.ui.fragments.CaretakersFragment;
import com.example.rentalhousingmanagementsystem.ui.fragments.RoomsFragment;
import com.example.rentalhousingmanagementsystem.ui.fragments.TenantsFragment;

import java.net.URISyntaxException;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private String Rental_id;


    public SectionsPagerAdapter(Context context, FragmentManager fm, String Rental_id) {
        super(fm);
        mContext = context;
        this.Rental_id = Rental_id;
    }

    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment.
        switch (position)
        {
            case 0:
                return TenantsFragment.newInstance(Rental_id,"");
            case 1:
                return RoomsFragment.newInstance(Rental_id,"");
            case 2:
                return CaretakersFragment.newInstance(Rental_id,"");
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}