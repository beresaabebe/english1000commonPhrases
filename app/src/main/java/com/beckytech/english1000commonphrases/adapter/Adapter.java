package com.beckytech.english1000commonphrases.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.english1000commonphrases.R;
import com.beckytech.english1000commonphrases.model.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ContentViewHolder> implements Filterable {

    public List<Model> modelList;
    public onClickedContent onClickedContent;
    public List<Model> filterModels;
    public Filter filter;
    @SuppressLint("NewApi")
    public Comparator<Model> sort = Comparator.comparing(Model::getTitle);

    public Adapter(List<Model> modelList, onClickedContent onClickedContent) {
        this.modelList = modelList;
        this.onClickedContent = onClickedContent;
        this.filterModels = modelList;
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

    @Override
    public Filter getFilter() {
        filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = filterModels;
                    results.count = filterModels.size();
                } else {
                    String search = constraint.toString().toLowerCase();
                    List<Model> searchModelList = new ArrayList<>();
                    for (Model model : filterModels) {
                        if (model.getTitle().toLowerCase().contains(search) ||
                                model.getContent().toLowerCase().contains(search)) {
                            searchModelList.add(model);
                        }
                    }
                    results.values = searchModelList;
                    results.count = searchModelList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                modelList = (List<Model>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    public interface onClickedContent {
        void itemClicked(Model model);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView title, subTitle;
        LinearLayout linear_item_list;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title.setSelected(true);
            subTitle = itemView.findViewById(R.id.subTitle);
            linear_item_list = itemView.findViewById(R.id.linear_item_list);
        }
    }
}
