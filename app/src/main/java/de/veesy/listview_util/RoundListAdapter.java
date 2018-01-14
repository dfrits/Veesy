package de.veesy.listview_util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.veesy.R;

/**
 * Created by dfritsch on 25.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class RoundListAdapter extends RecyclerView.Adapter<RoundListAdapter.MetaDataViewHolder> {
    private List<AdapterObject> data;
    private final ListItemCallback callback;

    /**
     * Setzt die neuen Daten und refresht die Liste.
     * @param data Liste mit Namen der gefundenen Geräten
     */
    public void setData(List<AdapterObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.data != null) this.data.clear();
        notifyDataSetChanged();
    }

    public RoundListAdapter(ListItemCallback callback) {
        this.callback = callback;
    }

    @Override
    public MetaDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.round_list_view_row, parent, false);
        return new MetaDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetaDataViewHolder holder, int position) {
        AdapterObject adapterObject = data.get(position);
        holder.bind(adapterObject.getItemName(), position, adapterObject.getImg());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MetaDataViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final TextView textView;
        private final ImageView imageView;
        private String item;
        private int position;

        MetaDataViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.lVDevices_row);
            imageView = itemView.findViewById(R.id.iV_FK_Image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.onItemClicked(position, item);
        }

        @Override
        public boolean onLongClick(View view) {
            callback.onItemLongClicked(position, item);
            return true;
        }

        void bind(String item, int position, Drawable image) {
            this.item = item;
            this.position = position;
            textView.setText(item);
            if (image != null) {
                imageView.setImageDrawable(image);
            }
        }
    }
}
