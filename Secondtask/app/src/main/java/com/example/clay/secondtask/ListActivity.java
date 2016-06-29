package com.example.clay.secondtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


/**
 * Created by clay on 17.04.16.
 */
public class ListActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    ViewPager pager;
    PagerAdapter pagerAdapter;
    ItemFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        if (savedInstanceState != null)
            //Restore the fragment's instance
            fragment = (ItemFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        else fragment = new ItemFragment();
    }

    protected void onResume(){
        super.onResume();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onListFragmentInteraction(ItemContent.Item item) {
        Intent ViewPagerActivity = new Intent(getApplicationContext(), ViewPagerFragment.class);
        ViewPagerActivity.putExtra("item", item.position);
        startActivity(ViewPagerActivity);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", fragment);
    }
}