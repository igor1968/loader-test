package com.igordanilchik.android.loader_test.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.igordanilchik.android.loader_test.loader.CatalogueLoader;
import com.igordanilchik.android.loader_test.ui.CategoriesContract;
import com.igordanilchik.android.loader_test.ui.fragment.AboutFragment;
import com.igordanilchik.android.loader_test.ui.fragment.CategoriesFragment;
import com.igordanilchik.android.loader_test.ui.fragment.ContactsFragment;
import com.igordanilchik.android.loader_test.ui.fragment.OfferFragment;
import com.igordanilchik.android.loader_test.ui.fragment.OffersFragment;
import com.igordanilchik.android.loader_test.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements CategoriesContract,
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
    Fragment currentFragment = null;


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

        if (savedInstanceState == null) {
            refreshData();

            MenuItem item = drawer.getMenu().findItem(R.id.nav_catalogue_fragment);
            selectDrawerItem(item);
        } else {
            if (savedInstanceState.get(ARG_CURRENT_FRAGMENT_TAG) != null) {
                currentFragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(ARG_CURRENT_FRAGMENT_TAG));
                if (currentFragment != null) {
                    updateDrawer();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (currentFragment != null) {
            bundle.putString(ARG_CURRENT_FRAGMENT_TAG, currentFragment.getTag());
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
            new LoaderProvider(this).add(data.getShop());
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
            case R.id.nav_contacts_fragment:
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = CategoriesFragment.class;
        }

        try {
            currentFragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error fragment load", e);
        }

        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, currentFragment, fragmentClass.getName())
                .commit();
    }

    public void selectDrawerItem(MenuItem item) {
        navigate(item.getItemId());

        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();
    }

    public void updateDrawer() {
        // update activity title and drawerLayout selection
        if (currentFragment instanceof CategoriesFragment && !currentFragment.isHidden()) {
            drawer.setCheckedItem(R.id.nav_catalogue_fragment);
            setTitle(drawer.getMenu().findItem(R.id.nav_catalogue_fragment).getTitle());
        } else if (currentFragment instanceof ContactsFragment && !currentFragment.isHidden()) {
            drawer.setCheckedItem(R.id.nav_contacts_fragment);
            setTitle(drawer.getMenu().findItem(R.id.nav_contacts_fragment).getTitle());
        } else if (currentFragment instanceof AboutFragment && !currentFragment.isHidden()) {
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
}
