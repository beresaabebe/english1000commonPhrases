package com.beckytech.english1000commonphrases;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.beckytech.english1000commonphrases.activity.AboutActivity;
import com.beckytech.english1000commonphrases.activity.DifferentCategoriesActivity;
import com.beckytech.english1000commonphrases.activity.PhraseDetailActivity;
import com.beckytech.english1000commonphrases.activity.ViewPagerDetailActivity;
import com.beckytech.english1000commonphrases.adapter.Adapter;
import com.beckytech.english1000commonphrases.adapter.AdapterViewPager;
import com.beckytech.english1000commonphrases.contents.CategoryContent;
import com.beckytech.english1000commonphrases.contents.ContentDetail;
import com.beckytech.english1000commonphrases.contents.ImageContents;
import com.beckytech.english1000commonphrases.contents.ImageTagNameContents;
import com.beckytech.english1000commonphrases.contents.OtherCatContent;
import com.beckytech.english1000commonphrases.contents.OtherCatImage;
import com.beckytech.english1000commonphrases.contents.SubTitleContent;
import com.beckytech.english1000commonphrases.contents.TitleContent;
import com.beckytech.english1000commonphrases.model.Model;
import com.beckytech.english1000commonphrases.model.ModelViewPager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onClickedContent, AdapterViewPager.onClickedContent, ViewPager2.PageTransformer {
    private InterstitialAd mInterstitialAd;
    private DrawerLayout drawerLayout;
    private List<Model> list;
    private final SubTitleContent subTitleContent = new SubTitleContent();
    private final TitleContent titleContent = new TitleContent();
    private final ContentDetail contentDetail = new ContentDetail();
    private final CategoryContent categoryContent = new CategoryContent();
    private Adapter adapter;
    private List<ModelViewPager> viewPagerList;
    private final ImageContents imageContents = new ImageContents();
    private final ImageTagNameContents tagNameContents = new ImageTagNameContents();
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;
    private final OtherCatContent otherCatContent = new OtherCatContent();
    private final OtherCatImage otherCatImage = new OtherCatImage();
    private List<ModelViewPager> secondaryViewPagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        AppRate.app_launched(this);

        MobileAds.initialize(this, initializationStatus -> {
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.fullScroll(View.FOCUS_DOWN);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {MenuOptions(item); return true;});

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        getData();
        adapter = new Adapter(list, this);
//        Collections.sort(list, adapter.sort);
        recyclerView.setAdapter(adapter);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager_main);
        viewPager2.setPageTransformer(this);
        getViewPagerData();
        AdapterViewPager adapterViewPager = new AdapterViewPager(viewPagerList, this);
        Collections.sort(viewPagerList, adapterViewPager.sort);
        viewPager2.setAdapter(adapterViewPager);

        ViewPager2 view_pager_other_cat_main = findViewById(R.id.view_pager_other_cat_main);
        getSecondaryViewPagerData();
        AdapterViewPager secondaryAdapterViewPager = new AdapterViewPager(secondaryViewPagerList, model -> startActivity(new Intent(this, DifferentCategoriesActivity.class).putExtra("data", model)));
        Collections.sort(secondaryViewPagerList, adapterViewPager.sort);
        view_pager_other_cat_main.setAdapter(secondaryAdapterViewPager);

    }

    private void getSecondaryViewPagerData() {
        secondaryViewPagerList = new ArrayList<>();
        for (int i = 0; i < otherCatImage.otherImages.length; i++) {

            String receivedValue = otherCatContent.otherCatContent[i].toLowerCase();
            StringBuilder stringBuilder = new StringBuilder(receivedValue);
            stringBuilder = stringBuilder.deleteCharAt(receivedValue.length() - 1);
            stringBuilder = stringBuilder.deleteCharAt(0);

            secondaryViewPagerList.add(new ModelViewPager(stringBuilder.substring(0,1).toUpperCase()+""+
                    stringBuilder.substring(1).toLowerCase(),otherCatImage.otherImages[i]));
        }
    }

    private void getViewPagerData() {
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < imageContents.images.length; i++) {
            viewPagerList.add(new ModelViewPager(tagNameContents.tagName[i].substring(0,1).toUpperCase()+""+
                    tagNameContents.tagName[i].substring(1), imageContents.images[i]));
        }
    }

    private void getData() {
        list = new ArrayList<>();
        for (int i = 0; i < titleContent.title.length; i++) {
            list.add(new Model(titleContent.title[i].substring(0,1).toUpperCase()+""+titleContent.title[i].substring(1).toLowerCase(),
                    subTitleContent.subTitle[i].substring(0,1).toUpperCase()+""+subTitleContent.subTitle[i].substring(1).toLowerCase(),
                    contentDetail.content[i],
                    categoryContent.categories[i]));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void MenuOptions(MenuItem item) {

        if (item.getItemId() == R.id.action_about_us) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();

                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        mInterstitialAd = null;
                        setAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                startActivity(new Intent(this, AboutActivity.class));
            }
        }

        if (item.getItemId() == R.id.action_rate) {
            String pkg = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
        }

        if (item.getItemId() == R.id.action_more_apps) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/developer?id=Beresa+Abebe")));
                        mInterstitialAd = null;
                        setAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=Beresa+Abebe")));
            }
        }

        if (item.getItemId() == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Download this app from Play store \n" + url);
            startActivity(Intent.createChooser(intent, "Choose to send"));
        }

        if (item.getItemId() == R.id.action_update) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            int lastVersion = pref.getInt("lastVersion", 0);
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            if (lastVersion < BuildConfig.VERSION_CODE) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                Toast.makeText(this, "New update is available download it from play store!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No update available!", Toast.LENGTH_SHORT).show();
            }
        }

        if (item.getItemId() == R.id.action_exit) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Close")
                    .setMessage("Do you want close?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        System.exit(0);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setBackground(getResources().getDrawable(R.drawable.nav_header_bg, null))
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuOptions(item);
        return true;
    }

    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.test_interstitial_ads_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Close")
                    .setMessage("Do you want to close?")
                    .setBackground(AppCompatResources.getDrawable(this,R.drawable.nav_header_bg))
                    .setPositiveButton("Close", (dialog, which) -> {
                        System.exit(0);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_menu, menu);
        MenuItem item = menu.findItem(R.id.actions_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        MenuOptions(item);
        return true;
    }

    @Override
    public void itemClicked(Model model) {
        int rand = (int) (Math.random() * 1000);
        if (rand % 2 == 0) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(MainActivity.this, PhraseDetailActivity.class).putExtra("data", model));
                        mInterstitialAd = null;
                        setAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                startActivity(new Intent(MainActivity.this, PhraseDetailActivity.class).putExtra("data", model));
            }
        }
        else {
            startActivity(new Intent(MainActivity.this, PhraseDetailActivity.class).putExtra("data", model));
        }
    }

    @Override
    public void itemClicked(ModelViewPager model) {
        startActivity(new Intent(MainActivity.this, ViewPagerDetailActivity.class).putExtra("data", model));
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if ( position < -1 ) { // [ -Infinity,-1 )
            // This page is way off-screen to the left.
            page.setAlpha( 0 );
        }
        else if ( position <= 1 ) { // [ -1,1 ]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max( MIN_SCALE, 1 - Math.abs( position ) );
            float vertMargin = pageHeight * ( 1 - scaleFactor ) / 2;
            float horzMargin = pageWidth * ( 1 - scaleFactor ) / 2;
            if ( position < 0 ) {
                page.setTranslationX( horzMargin - vertMargin / 2 );
            } else {
                page.setTranslationX( -horzMargin + vertMargin / 2 );
            }

            // Scale the page down ( between MIN_SCALE and 1 )
            page.setScaleX( scaleFactor );
            page.setScaleY( scaleFactor );

            // Fade the page relative to its size.
            page.setAlpha( MIN_ALPHA +
                    ( scaleFactor - MIN_SCALE ) /
                            ( 1 - MIN_SCALE ) * ( 1 - MIN_ALPHA ));

        } else { // ( 1,+Infinity ]
            // This page is way off-screen to the right.
            page.setAlpha( 0 );
        }

    }
}