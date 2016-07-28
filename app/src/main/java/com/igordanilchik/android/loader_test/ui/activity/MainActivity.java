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

import com.igordanilchik.android.loader_test.R;
import com.igordanilchik.android.loader_test.loader.CatalogueLoader;
import com.igordanilchik.android.loader_test.model.Catalogue;
import com.igordanilchik.android.loader_test.model.Category;
import com.igordanilchik.android.loader_test.model.Offer;
import com.igordanilchik.android.loader_test.model.Shop;
import com.igordanilchik.android.loader_test.ui.fragment.AboutFragment;
import com.igordanilchik.android.loader_test.ui.fragment.CategoriesFragment;
import com.igordanilchik.android.loader_test.ui.fragment.ContactsFragment;
import com.igordanilchik.android.loader_test.ui.fragment.OffersFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Catalogue>,
        CategoriesFragment.OnContentUpdate, OffersFragment.OnContentUpdate {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String ARG_CURRENT_FRAGMENT_TAG = "ARG_CURRENT_FRAGMENT_TAG";
    public static final String ARG_DATA = "ARG_DATA";


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView drawer;
    ActionBarDrawerToggle drawerToggle;
    @Nullable
    Fragment currentFragment = null;
    @Nullable
    private Shop dataset;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return false;
                    }
                }
        );

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            getSupportLoaderManager().initLoader(0, null, this);

            MenuItem item = drawer.getMenu().findItem(R.id.nav_catalogue_fragment);
            selectDrawerItem(item);
        } else {
            if (savedInstanceState.get(ARG_CURRENT_FRAGMENT_TAG) != null) {
                currentFragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(ARG_CURRENT_FRAGMENT_TAG));
                if (currentFragment != null) {
                    updateDrawer();
                }
            }
            if (savedInstanceState.get(ARG_DATA) != null) {
                dataset = Parcels.unwrap(savedInstanceState.getParcelable(ARG_DATA));
            } else {
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (currentFragment != null) {
            bundle.putString(ARG_CURRENT_FRAGMENT_TAG, currentFragment.getTag());
        }
        if (dataset != null) {
            bundle.putParcelable(ARG_DATA, Parcels.wrap(dataset));
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
            dataset = data.getShop();
            if (dataset.getCategories() != null) {
                CategoriesFragment fragment = (CategoriesFragment) getSupportFragmentManager().findFragmentByTag(CategoriesFragment.class.getName());
                if (fragment != null) {
                    fragment.updateContent(getContent());
                }
            }
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
    @Nullable
    public List<Category> getContent() {
        if (dataset != null) {
            List<Category> categories = dataset.getCategories();
            if (dataset.getOffers() != null) {
                List<Offer> offers = dataset.getOffers();
                for (Category category : categories) {
                    int id = category.getId();
                    for (Offer offer : offers) {
                        if (offer.getCategoryId() == category.getId() && offer.getPictureUrl() != null) {
                            category.setPictureUrl(offer.getPictureUrl());
                            break;
                        }
                    }
                }
            }
            return categories;
        }
        return null;
    }

    @Nullable
    @Override
    public List<Offer> getContent(int categoryId) {
        if (dataset != null && dataset.getOffers() != null) {
            ArrayList<Offer> offers = new ArrayList<>();
            for (Offer offer : dataset.getOffers()) {
                if (offer.getCategoryId() == categoryId) {
                    offers.add(offer);
                }
            }
            return offers;
        }
        return null;
    }
}
