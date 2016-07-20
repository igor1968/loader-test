package com.igordanilchik.android.loader_test.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.ui.fragment.AboutFragment;
import com.igordanilchik.android.loader_test.ui.fragment.CatalogueFragment;
import com.igordanilchik.android.loader_test.ui.fragment.ContactsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ARG_CURRENT_FRAGMENT_TAG = "ARG_CURRENT_FRAGMENT_TAG";

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.menu_drawer)
    NavigationView mDrawer;
    ActionBarDrawerToggle mDrawerToggle;
    @Nullable
    Fragment mCurrentFragment = null;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return false;
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            MenuItem item = mDrawer.getMenu().findItem(R.id.nav_catalogue_fragment);
            selectDrawerItem(item);
        } else if (savedInstanceState.get(ARG_CURRENT_FRAGMENT_TAG) != null) {
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(ARG_CURRENT_FRAGMENT_TAG));
            if (mCurrentFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_content, mCurrentFragment, mCurrentFragment.getTag())
//                        .addToBackStack(mCurrentFragment.getTag())
                        .commit();
                updateDrawer();
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mCurrentFragment != null) {
            bundle.putString(ARG_CURRENT_FRAGMENT_TAG, mCurrentFragment.getTag());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void navigate(int id) {
        Class fragmentClass;
        switch (id) {
            case R.id.nav_catalogue_fragment:
                fragmentClass = CatalogueFragment.class;
                break;
            case R.id.nav_contacts_fragment:
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = CatalogueFragment.class;
        }

        try {
            mCurrentFragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error fragment load", e);
        }

        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, mCurrentFragment, fragmentClass.getName())
//                .addToBackStack(fragmentClass.getName())
                .commit();
    }

    public void selectDrawerItem(MenuItem item) {
        navigate(item.getItemId());

        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
    }

    public void updateDrawer() {
        // update activity title and mDrawerLayout selection
        if (mCurrentFragment instanceof CatalogueFragment && !mCurrentFragment.isHidden()) {
            mDrawer.setCheckedItem(R.id.nav_catalogue_fragment);
            setTitle(mDrawer.getMenu().findItem(R.id.nav_catalogue_fragment).getTitle());
        } else if (mCurrentFragment instanceof ContactsFragment && !mCurrentFragment.isHidden()) {
            mDrawer.setCheckedItem(R.id.nav_contacts_fragment);
            setTitle(mDrawer.getMenu().findItem(R.id.nav_contacts_fragment).getTitle());
        } else if (mCurrentFragment instanceof AboutFragment && !mCurrentFragment.isHidden()) {
            mDrawer.setCheckedItem(R.id.nav_about_fragment);
            setTitle(mDrawer.getMenu().findItem(R.id.nav_about_fragment).getTitle());
        }
    }
}
