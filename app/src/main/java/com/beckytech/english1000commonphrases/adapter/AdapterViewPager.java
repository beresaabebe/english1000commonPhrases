package com.beckytech.english1000commonphrases.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.english1000commonphrases.R;
import com.beckytech.english1000commonphrases.model.Model;
import com.beckytech.english1000commonphrases.model.ModelViewPager;

import java.util.List;

public class AdapterViewPager extends RecyclerView.Adapter<AdapterViewPager.ViewPageHolder> {

    private final List<ModelViewPager> viewPagerList;
    public onClickedContent onClickedContent;

    public AdapterViewPager(List<ModelViewPager> viewPagerList, onClickedContent onClickedContent) {
        this.viewPagerList = viewPagerList;
        this.onClickedContent = onClickedContent;
    }

    public interface onClickedContent {
        void itemClicked(ModelViewPager model);
    }

    @NonNull
    @Override
    public ViewPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPageHolder holder, int position) {
        ModelViewPager modelViewPager = viewPagerList.get(position);
        holder.textView.setText(modelViewPager.getTag());
        holder.imageView.setImageResource(modelViewPager.getImage());
        holder.itemView.setOnClickListener(v -> onClickedContent.itemClicked(modelViewPager));
    }

    @Override
    public int getItemCount() {
        return viewPagerList.size();
    }

    protected static class ViewPageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewPageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.view_pager_image);
            textView = itemView.findViewById(R.id.view_pager_title);
        }
    }
}
