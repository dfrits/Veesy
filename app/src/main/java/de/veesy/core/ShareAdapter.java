package de.veesy.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.veesy.R;

/**
 * Created by dfritsch on 25.10.2017.
 * veesy.de
 * hs-augsburg
 */

class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.MetaDataViewHolder> {
    private List<String> deviceNames;
    private final Callback callback;

    public interface Callback {
        void onDeviceClicked(int position, String deviceName);
    }

    /**
     * Setzt die neuen Daten und refresht die Liste.
     * @param deviceNames Liste mit Namen der gefundenen Geräten
     */
    void setDeviceNames(List<String> deviceNames) {
        this.deviceNames = deviceNames;
        notifyDataSetChanged();
    }

    ShareAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    public MetaDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.device_list_view, parent, false);
        return new MetaDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetaDataViewHolder holder, int position) {
        holder.bind(deviceNames.get(position), position);
    }

    @Override
    public int getItemCount() {
        return deviceNames == null ? 0 : deviceNames.size();
    }

    public class MetaDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private String item;
        private int position;

        MetaDataViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.lVDevices_row);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.onDeviceClicked(position, item);
        }

        void bind(String item, int position) {
            this.item = item;
            this.position = position;
            textView.setText(item);
        }
    }
}
