package com.qr.core.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {
    private List<RecyclerViewModel> models = new ArrayList<>();

    public void setModels(List<RecyclerViewModel> models){
        this.models.clear();
        this.models.addAll(models);
        notifyDataSetChanged();
    }
    public void addModels(List<RecyclerViewModel> models){
        this.models.addAll(models);
        notifyItemRangeInserted(this.models.size(),models.size());
    }
    public void addModels(int position,List<RecyclerViewModel> models){
        this.models.addAll(models);
        notifyItemRangeInserted(position,models.size());
    }
    public void addModel(RecyclerViewModel model){
        models.add(model);
        notifyItemInserted(models.size());
    }
    public void addModel(int position,RecyclerViewModel model){
        models.add(position,model);
        notifyItemInserted(position);
    }
    public void removeModel(RecyclerViewModel model){
        int index = models.indexOf(model);
        if(index >= 0){
            models.remove(index);
            notifyItemRemoved(index);
        }
    }
    public void clearModels(){
        models.clear();
        notifyDataSetChanged();
    }
    public void removeModel(int index){
        if(index >= 0 && index < models.size()){
            models.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void replaceModel(int index,RecyclerViewModel model){
        if(index >= 0 && index < models.size()) {
            models.set(index,model);
            notifyItemChanged(index);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return models.get(position).layoutId();
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position, @NonNull List<Object> payloads) {
        models.get(position).bindView(holder,position,payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            final GridLayoutManager.SpanSizeLookup lookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return models.get(position).spinSize();
                }
            };
            gridLayoutManager.setSpanSizeLookup(lookup);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }
}
