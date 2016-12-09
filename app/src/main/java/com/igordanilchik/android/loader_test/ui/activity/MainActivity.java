package com.igordanilchik.android.loader_test.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.data.Catalogue;
import com.igordanilchik.android.loader_test.data.source.LoaderProvider;
import com.igordanilchik.android.loader_test.data.source.local.LocalDataSource;
import com.igordanilchik.android.loader_test.data.source.remote.CatalogueLoader;
import com.igordanilchik.android.loader_test.ui.ViewContract;
import com.igordanilchik.android.loader_test.ui.fragment.AboutFragment;
import com.igordanilchik.android.loader_test.ui.fragment.CategoriesFragment;
import com.igordanilchik.android.loader_test.ui.fragment.OfferFragment;
import com.igordanilchik.android.loader_test.ui.fragment.OffersFragment;
import com.igordanilchik.android.loader_test.ui.fragment.SettingsFragment;
import com.igordanilchik.android.loader_test.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements ViewContract,
        LoaderManager.LoaderCallbacks<Catalogue> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String ARG_CURRENT_FRAGMENT_TAG = "ARG_CURRENT_FRAGMENT_TAG";
    public static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    public static final String ARG_OFFER_ID = "ARG_OFFER_ID";
    private static final int CATALOGUE_LOADER = 0;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView drawer;
    @BindView(R.id.empty_state_container)
    LinearLayout emptyStateContainer;

    ActionBarDrawerToggle drawerToggle;
    @Nullable
    String currentTag;

    LoaderProvider loaderProvider;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        drawer.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return false;
                }
        );

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        loaderProvider = new LoaderProvider(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (savedInstanceState == null) {
            refreshData();

            MenuItem item = drawer.getMenu().findItem(R.id.nav_catalogue_fragment);
            selectDrawerItem(item);
        } else {
            if (savedInstanceState.get(ARG_CURRENT_FRAGMENT_TAG) != null) {
                currentTag = savedInstanceState.getString(ARG_CURRENT_FRAGMENT_TAG);
                if (currentTag != null) {
                    updateDrawer();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (currentTag != null) {
            bundle.putString(ARG_CURRENT_FRAGMENT_TAG, currentTag);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public Loader<Catalogue> onCreateLoader(int id, Bundle args) {
        return new CatalogueLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Catalogue> loader, Catalogue data) {
        if (data != null && data.getShop() != null) {
            LocalDataSource.getInstance(getContentResolver()).add(data.getShop());
        }
    }

    @Override
    public void onLoaderReset(Loader<Catalogue> loader) {
    }

    public void navigate(int id) {
        Class fragmentClass;
        switch (id) {
            case R.id.nav_catalogue_fragment:
                fragmentClass = CategoriesFragment.class;
                break;
            case R.id.nav_preferences_fragment:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = CategoriesFragment.class;
        }
        clearBackstack();

        try {
            Fragment currentFragment = (Fragment) fragmentClass.newInstance();
            // Insert the fragment by replacing any existing fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, currentFragment, fragmentClass.getName())
                    .commit();
            this.currentTag = fragmentClass.getName();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error fragment load", e);
        }
    }

    public void selectDrawerItem(MenuItem item) {
        navigate(item.getItemId());

        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

    public void updateDrawer() {
        if (currentTag.equals(CategoriesFragment.class.getName())) {
            drawer.setCheckedItem(R.id.nav_catalogue_fragment);
            setTitle(drawer.getMenu().findItem(R.id.nav_catalogue_fragment).getTitle());
        } else if (currentTag.equals(SettingsFragment.class.getName())) {
            drawer.setCheckedItem(R.id.nav_preferences_fragment);
            setTitle(drawer.getMenu().findItem(R.id.nav_preferences_fragment).getTitle());
        } else if (currentTag.equals(AboutFragment.class.getName())) {
            drawer.setCheckedItem(R.id.nav_about_fragment);
            setTitle(drawer.getMenu().findItem(R.id.nav_about_fragment).getTitle());
        }
    }

    @Override
    public void refreshData() {
        getSupportLoaderManager().initLoader(CATALOGUE_LOADER, null, this);
    }

    @Override
    public void showCategory(int categoryId) {
        OffersFragment fragment = OffersFragment.newInstance(categoryId);
        FragmentUtils.replaceFragment(this, R.id.frame_content, fragment, true);

    }

    @Override
    public void showOffer(int offerId) {
        OfferFragment fragment = OfferFragment.newInstance(offerId);
        FragmentUtils.replaceFragment(this, R.id.frame_content, fragment, true);
    }

    @Override
    public void showEmptyState() {
        emptyStateContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyState() {
        emptyStateContainer.setVisibility(View.GONE);
    }

    @Override
    public LoaderProvider getLoaderProvider() {
        return loaderProvider;
    }

    private void clearBackstack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(0);
            getSupportFragmentManager().popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().executePendingTransactions();
        }
    }
}
