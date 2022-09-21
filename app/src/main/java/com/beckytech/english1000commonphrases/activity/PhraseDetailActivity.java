package com.beckytech.english1000commonphrases.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.english1000commonphrases.R;
import com.beckytech.english1000commonphrases.adapter.AdapterDetail;
import com.beckytech.english1000commonphrases.contents.CategoryContent;
import com.beckytech.english1000commonphrases.contents.ContentDetail;
import com.beckytech.english1000commonphrases.model.Model;
import com.beckytech.english1000commonphrases.model.ModelDetail;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhraseDetailActivity extends AppCompatActivity {

    private List<ModelDetail> modelDetails;
    private final CategoryContent categoryContent = new CategoryContent();
    private final ContentDetail contentDetail = new ContentDetail();
    private Model model;
    private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_detail);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
        RecyclerView recyclerView = findViewById(R.id.recyclerView_detail);

        TextView rev_title_detail_phrase = findViewById(R.id.rev_title_detail_phrase);

        Intent intent = getIntent();
        model = (Model) intent.getSerializableExtra("data");
        modelDetails = new ArrayList<>();
        TextView textView = findViewById(R.id.title_detail);
        textView.setText(model.getSubTitle());
        textView.setSelected(true);
        getData();
        AdapterDetail adapterDetail = new AdapterDetail(this, modelDetails);
        recyclerView.setAdapter(adapterDetail);

        rev_title_detail_phrase.setText(model.getTitle());
        rev_title_detail_phrase.setOnClickListener(v -> textToSpeech = new TextToSpeech(this, status -> {
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(model.getTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
        }));
    }

    private void getData() {
        String cat = model.getCategories().toLowerCase();
        for (int i = 0; i < categoryContent.categories.length; i++) {
            if (categoryContent.categories[i].toLowerCase().equals(cat)) {
                modelDetails.add(new ModelDetail(contentDetail.content[i].substring(0,1).toUpperCase()+""+contentDetail.content[i].substring(1)));
            }
        }
    }

}