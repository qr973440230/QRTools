package com.qr.core.tools.adapter.recyclerview;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public RecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = itemView.findViewById(viewId);
            mViews.put(viewId,view);
        }

        return cast(view);
    }

    public RecyclerViewViewHolder setAlpha(int viewId, int value){
        getView(viewId).setAlpha(value);
        return this;
    }

    public RecyclerViewViewHolder setVisible(int viewId, int visible){
        getView(viewId).setVisibility(visible);
        return this;
    }

    public RecyclerViewViewHolder setTag(int viewId, Object object){
        getView(viewId).setTag(object);
        return this;
    }

    public RecyclerViewViewHolder setTag(int viewId, int key, Object object){
        getView(viewId).setTag(key,object);
        return this;
    }

    public RecyclerViewViewHolder setOnClickListener(int viewId, View.OnClickListener listener){
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public RecyclerViewViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener){
        getView(viewId).setOnTouchListener(listener);
        return this;
    }

    public RecyclerViewViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener){
        getView(viewId).setOnLongClickListener(listener);
        return this;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj){
        return (T) obj;
    }
}
