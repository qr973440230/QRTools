package com.qr.core.adapter.recyclerview;

import java.util.List;

public abstract class RecyclerViewModel{
    public abstract int spinSize();
    public abstract int layoutId();
    public abstract void bindView(RecyclerViewViewHolder holder, int position, List<Object> payloads);
}
