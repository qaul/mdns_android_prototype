/**
 * Copyright (C) 2021 Terminator712
 */
package com.mdnsdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;


public class TxtRecordsAdapter extends RecyclerView.Adapter<TxtRecordsAdapter.ViewHolder> {

    private final int mBackground;
    private final SimpleArrayMap<String, String> ipRecords = new SimpleArrayMap<>();
    private final SimpleArrayMap<String, String> txtRecords = new SimpleArrayMap<>();

    public TxtRecordsAdapter(Context context) {
        TypedValue mTypedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.two_text_item, viewGroup, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.text1.setText(getKey(position));
        holder.text2.setText(getValue(position));
        holder.itemView.setOnClickListener(v -> TxtRecordsAdapter.this.onItemClick(v, position));
    }

    public void onItemClick(View view, int position){
        Context context = view.getContext();

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getKey(position), getValue(position));
        clipboard.setPrimaryClip(clip);

        Snackbar snackbar = Snackbar.make(view, "Copied", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.accent);
        snackbar.show();
    }

    @Override
    public int getItemCount() {
        return ipRecords.size() + txtRecords.size();
    }

    protected String getKey(int position) {
        if (position < ipRecords.size()) {
            return ipRecords.keyAt(position);
        }
        else {
            return txtRecords.keyAt(position - ipRecords.size());
        }
    }

    protected String getValue(int position) {
        if (position < ipRecords.size()) {
            return ipRecords.valueAt(position);
        }
        else {
            return txtRecords.valueAt(position - ipRecords.size());
        }
    }

    public void swapIPRecords(ArrayMap<String, String> records) {
        this.ipRecords.clear();
        this.ipRecords.putAll(records);
    }

    public void swapTXTRecords(ArrayMap<String, String> records) {
        this.txtRecords.clear();
        this.txtRecords.putAll(records);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        public TextView text2;

        ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
}
