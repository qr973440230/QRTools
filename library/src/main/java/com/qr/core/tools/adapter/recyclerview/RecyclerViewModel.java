package com.qr.core.tools.adapter.recyclerview;


import java.util.List;

public abstract class RecyclerViewModel<T> {
    private final T data;
    public RecyclerViewModel(T data) {
        this.data = data;
    }

    public abstract int spinSize();
    public abstract int layoutId();
    public abstract void bindView(RecyclerViewViewHolder holder, int position, List<Object> payloads);
    public T getData() {
        return data;
    }
}
