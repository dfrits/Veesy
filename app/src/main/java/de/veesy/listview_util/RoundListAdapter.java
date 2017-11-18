package de.veesy.listview_util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.veesy.R;

/**
 * Created by dfritsch on 25.10.2017.
 * veesy.de
 * hs-augsburg
 */
//TODO Klasse generisch machen, sodass sie individuell für BT-Device und Contact verwendet werden kann
public class RoundListAdapter extends RecyclerView.Adapter<RoundListAdapter.MetaDataViewHolder> {
    private List<String> data;
    private Drawable dummyimage = null;
    private final ListItemCallback callback;

    /**
     * Setzt die neuen Daten und refresht die Liste.
     * @param data Liste mit Namen der gefundenen Geräten
     */
    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * Übergibt testweise ein Bild an den Adapter.
     * @param data Liste
     * @param dummyimage Dummybild
     */
    //TODO Nach Generischierung wieder entfernen
    public void setDataWithDummyImage(List<String> data, Drawable dummyimage) {
        this.data = data;
        this.dummyimage = dummyimage;
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
        holder.bind(data.get(position), position, dummyimage);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
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
            callback.onItemClicked(position, item);
        }

        void bind(String item, int position, Drawable dummyimage) {
            this.item = item;
            this.position = position;
            textView.setText(item);
            if (dummyimage != null) {
                textView.setCompoundDrawablesWithIntrinsicBounds(dummyimage, null, null, null);
            }
        }
    }
}
