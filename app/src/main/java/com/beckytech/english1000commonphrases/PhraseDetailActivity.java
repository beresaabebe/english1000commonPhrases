package com.beckytech.english1000commonphrases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhraseDetailActivity extends AppCompatActivity {

    private List<ModelDetail> modelDetails;
    private final CategoryContent categoryContent = new CategoryContent();
    private final ContentDetail contentDetail = new ContentDetail();
    private Model model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_detail);
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        Intent intent = getIntent();
        model = (Model) intent.getSerializableExtra("data");
        modelDetails = new ArrayList<>();
        TextView textView = findViewById(R.id.tv_title);
        textView.setText(model.getTitle());
        getData();
        AdapterDetail adapterDetail = new AdapterDetail(this, modelDetails);
        recyclerView.setAdapter(adapterDetail);
    }

    private void getData() {
        String str = model.getCategories();
        for (int i = 0; i < categoryContent.categories.length; i++) {
            if (categoryContent.categories[i].toLowerCase().equals(str)) {
                modelDetails.add(new ModelDetail(contentDetail.content[i].substring(0,1).toUpperCase()+""+contentDetail.content[i].substring(1)));
            }
        }
    }

}