package com.thebipolaroptimist.projecttwo.models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.EntryListActivity;
import com.thebipolaroptimist.projecttwo.R;


public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>
{

    Entry[] entries;
    Context context;

    public EntryAdapter(Entry[] entries, Context context)
    {
        this.entries = entries;
        this.context = context;
    }
    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder holder, int position) {
        holder.entryNote.setText(entries[position].getEntryNote());
        holder.entryTimestamp.setText(entries[position].getEntrySummary());
    }

    @Override
    public int getItemCount() {
        return entries.length;
    }

    public String getEntryId(int position)
    {
        return entries[position].getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private static final String TAG = "ViewHolder";
        public TextView entryNote;
        public TextView entryTimestamp;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            entryNote = itemView.findViewById(R.id.entry_text);
            entryTimestamp = itemView.findViewById(R.id.entry_timestamp);
            LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.entry_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EntryListActivity) context).openEntryForEdit(getAdapterPosition());
                }
            });
        }
    }
}

/*
public class EntryAdapter extends RealmRecyclerViewAdapter<Entry,EntryAdapter.ViewHolder> {
    Context mContext;

    public EntryAdapter(@Nullable OrderedRealmCollection<Entry> data, boolean autoUpdate, Context context) {
        super(data, autoUpdate);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.entryNote.setText(getData().get(position).getEntryNote());
        holder.entryTimestamp.setText(getData().get(position).getEntrySummary());
    }

    public String getEntryId(int position)
    {
        return getData().get(position).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private static final String TAG = "ViewHolder";
        public TextView entryNote;
        public TextView entryTimestamp;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            entryNote = itemView.findViewById(R.id.entry_text);
            entryTimestamp = itemView.findViewById(R.id.entry_timestamp);
            LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.entry_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EntryListActivity) context).openEntryForEdit(getAdapterPosition());
                }
            });
        }
    }
}*/