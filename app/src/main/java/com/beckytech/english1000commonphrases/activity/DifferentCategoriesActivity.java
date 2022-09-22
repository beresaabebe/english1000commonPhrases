package com.beckytech.english1000commonphrases.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.english1000commonphrases.R;
import com.beckytech.english1000commonphrases.adapter.AdapterDetail;
import com.beckytech.english1000commonphrases.contents.ContentDetail;
import com.beckytech.english1000commonphrases.model.ModelDetail;
import com.beckytech.english1000commonphrases.model.ModelViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DifferentCategoriesActivity extends AppCompatActivity {

    private ModelViewPager modelViewPager;
    private List<ModelDetail> modelList;
    private final ContentDetail contentDetail = new ContentDetail();

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

        title.setText(String.format("Useful Phrases of %s%s words", receivedValue.substring(0, 1).toUpperCase(), receivedValue.substring(1)));

        rev_title_different_categories.setText(String.format("Phrases of %s", receivedValue.substring(0,1).toUpperCase()+""+
                receivedValue.substring(1)));
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