package com.beckytech.english1000commonphrases.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.english1000commonphrases.activity.SearchGoogleActivity;
import com.beckytech.english1000commonphrases.model.ModelDetail;
import com.beckytech.english1000commonphrases.R;

import java.util.List;
import java.util.Locale;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.DetailViewHolder> {

    private final List<ModelDetail> modelDetails;
    private final Context context;
    private TextToSpeech textToSpeech;

    public AdapterDetail(Context context, List<ModelDetail> modelDetails) {
        this.modelDetails = modelDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        ModelDetail modelDetail = modelDetails.get(position);
        holder.text_original.setText(modelDetail.getTextOriginal());
        String word = modelDetail.getTextOriginal();
        holder.play_btn.setOnClickListener(v -> {
            textToSpeech = new TextToSpeech(context, status -> {
               if (status != TextToSpeech.ERROR) {
                   textToSpeech.setLanguage(Locale.ENGLISH);
                   textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
               }
            });
        });

        holder.text_original.setOnClickListener(v -> {
            context.startActivity(new Intent(context, SearchGoogleActivity.class).putExtra("query", word));
        });
    }

    @Override
    public int getItemCount() {
        return modelDetails.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView text_original;
        ImageView play_btn;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            text_original = itemView.findViewById(R.id.text_original);
            play_btn = itemView.findViewById(R.id.btn_play);

        }
    }
}
