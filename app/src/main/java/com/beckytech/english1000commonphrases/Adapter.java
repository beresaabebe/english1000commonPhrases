package com.beckytech.english1000commonphrases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ContentViewHolder> {

    private final List<Model> modelList;
    private final onClickedContent onClickedContent;

    public Adapter(List<Model> modelList, onClickedContent onClickedContent) {
        this.modelList = modelList;
        this.onClickedContent = onClickedContent;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Model model = modelList.get(position);
        holder.subTitle.setText(model.getSubTitle());
        holder.title.setText(model.getTitle());
        holder.itemView.setOnClickListener(v -> onClickedContent.itemClicked(model));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public interface onClickedContent {
        void itemClicked(Model model);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView title, subTitle;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title.setSelected(true);
            subTitle = itemView.findViewById(R.id.subTitle);
        }
    }
}
