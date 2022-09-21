package com.beckytech.english1000commonphrases.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.beckytech.english1000commonphrases.R;
import com.beckytech.english1000commonphrases.adapter.Adapter;
import com.beckytech.english1000commonphrases.contents.CategoryContent;
import com.beckytech.english1000commonphrases.contents.ContentDetail;
import com.beckytech.english1000commonphrases.contents.SubTitleContent;
import com.beckytech.english1000commonphrases.contents.TitleContent;
import com.beckytech.english1000commonphrases.model.Model;
import com.beckytech.english1000commonphrases.model.ModelViewPager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerDetailActivity extends AppCompatActivity implements Adapter.onClickedContent {

    private ModelViewPager modelViewPager;
    private List<Model> modelList;
    private final TitleContent titleContent = new TitleContent();
    private final SubTitleContent subTitleContent = new SubTitleContent();
    private final ContentDetail contentDetail = new ContentDetail();
    private final CategoryContent categoryContent = new CategoryContent();
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_detail);

        findViewById(R.id.back_button_view_pager_detail).setOnClickListener(v -> onBackPressed());

        RecyclerView recyclerView = findViewById(R.id.recyclerView_view_pager_detail);
        Intent intent = getIntent();
        modelViewPager = (ModelViewPager) intent.getSerializableExtra("data");
        getData();
        Adapter adapter = new Adapter(modelList, this);
        TextView title = findViewById(R.id.tv_title_view_pager_detail);
        title.setText(String.format("Useful Phrases at %s%s", modelViewPager.getTag().substring(0, 1).toUpperCase(), modelViewPager.getTag().substring(1)));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        modelList = new ArrayList<>();
        for (int i = 0; i < titleContent.title.length; i++) {
            if (subTitleContent.subTitle[i].toLowerCase().contains(modelViewPager.getTag().toLowerCase())) {
                modelList.add(new Model(titleContent.title[i].substring(0, 1).toUpperCase() + "" + titleContent.title[i].substring(1).toLowerCase(),
                        subTitleContent.subTitle[i].substring(0, 1).toUpperCase() + "" + subTitleContent.subTitle[i].substring(1).toLowerCase(),
                        contentDetail.content[i],
                        categoryContent.categories[i]));
            }
        }
    }

    @Override
    public void itemClicked(Model model) {
        int rand = (int) (Math.random() * 1000);
        if (rand % 2 == 0) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(ViewPagerDetailActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(ViewPagerDetailActivity.this, PhraseDetailActivity.class).putExtra("data", model));
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
                startActivity(new Intent(ViewPagerDetailActivity.this, PhraseDetailActivity.class).putExtra("data", model));
            }
        }
        else {
            startActivity(new Intent(ViewPagerDetailActivity.this, PhraseDetailActivity.class).putExtra("data", model));
        }
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
}