package com.thebipolaroptimist.projecttwo.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;

public class EntryAdapter extends  RecyclerView.Adapter<EntryAdapter.ViewHolder>{
    private Entry[] mEntries;

    public EntryAdapter(Entry[] entries)
    {
        mEntries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.entryView.setText(mEntries[position].getEntryName());
    }

    @Override
    public int getItemCount() {
        return mEntries.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView entryView;
        public ViewHolder(View itemView) {
            super(itemView);
            entryView = (TextView) itemView.findViewById(R.id.entry_text);
        }
    }
}
