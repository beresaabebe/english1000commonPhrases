package com.beckytech.english1000commonphrases.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.beckytech.english1000commonphrases.R;
import com.beckytech.english1000commonphrases.adapter.Adapter;
import com.beckytech.english1000commonphrases.adapter.AdapterDetail;
import com.beckytech.english1000commonphrases.contents.CategoryContent;
import com.beckytech.english1000commonphrases.contents.ContentDetail;
import com.beckytech.english1000commonphrases.contents.SubTitleContent;
import com.beckytech.english1000commonphrases.contents.TitleContent;
import com.beckytech.english1000commonphrases.model.Model;
import com.beckytech.english1000commonphrases.model.ModelDetail;
import com.beckytech.english1000commonphrases.model.ModelViewPager;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DifferentCategoriesActivity extends AppCompatActivity {

    private ModelViewPager modelViewPager;
    private List<ModelDetail> modelList;
    private final TitleContent titleContent = new TitleContent();
    private final SubTitleContent subTitleContent = new SubTitleContent();
    private final ContentDetail contentDetail = new ContentDetail();
    private final CategoryContent categoryContent = new CategoryContent();
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different_categories);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_different_categories);
        Intent intent = getIntent();
        modelViewPager = (ModelViewPager) intent.getSerializableExtra("data");

        findViewById(R.id.back_button_different_categories).setOnClickListener(v -> onBackPressed());

        TextView title = findViewById(R.id.title_different_categories);
        TextView rev_title_different_categories = findViewById(R.id.rev_title_different_categories);

        String receivedValue = modelViewPager.getTag().toLowerCase();
        StringBuilder stringBuilder = new StringBuilder(receivedValue);
        stringBuilder = stringBuilder.deleteCharAt(receivedValue.length() - 1);
        stringBuilder = stringBuilder.deleteCharAt(0);

        title.setText(String.format("Useful Phrases of %s%s words", stringBuilder.substring(0, 1).toUpperCase(), stringBuilder.substring(1)));

        rev_title_different_categories.setText(String.format("Phrases of %s", stringBuilder.substring(0,1).toUpperCase()+""+
                stringBuilder.substring(1)));
        getDataOfContentDetail();

        AdapterDetail adapterDetail = new AdapterDetail(this, modelList);
        Collections.sort(modelList, adapterDetail.sort);
        recyclerView.setAdapter(adapterDetail);
    }

    private void getDataOfContentDetail() {
        modelList = new ArrayList<>();
        String receivedData = modelViewPager.getTag().toLowerCase();
        for (int i = 0; i < contentDetail.content.length; i++) {
            if (contentDetail.content[i].toLowerCase().contains(receivedData)) {
                modelList.add(new ModelDetail(contentDetail.content[i].substring(0,1).toUpperCase()+""+
                        contentDetail.content[i].substring(1)));
            }
        }
    }
}