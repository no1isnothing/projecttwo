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


public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    final private Entry[] mEntries;
    final private Context mContext;

    public EntryAdapter(Entry[] entries, Context context) {
        mEntries = entries;
        mContext = context;
    }

    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new ViewHolder(v, mContext);
    }

    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder holder, int position) {
        holder.entryNote.setText(mEntries[position].getEntryNote());
        holder.entryTimestamp.setText(mEntries[position].getEntrySummary());
    }

    @Override
    public int getItemCount() {
        return mEntries.length;
    }

    public String getEntryId(int position) {
        if(position < mEntries.length -1) {
            return mEntries[position].getId();
        }
        return "";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        final private TextView entryNote;
        final private TextView entryTimestamp;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            entryNote = itemView.findViewById(R.id.entry_text);
            entryTimestamp = itemView.findViewById(R.id.entry_timestamp);
            LinearLayout layout = itemView.findViewById(R.id.entry_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EntryListActivity) context).openEntryForEdit(getAdapterPosition());
                }
            });
        }
    }
}