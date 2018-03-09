package com.thebipolaroptimist.projecttwo.models;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.EntryListActivity;
import com.thebipolaroptimist.projecttwo.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

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
}
