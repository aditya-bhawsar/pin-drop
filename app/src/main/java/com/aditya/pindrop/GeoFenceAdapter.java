package com.aditya.pindrop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class GeoFenceAdapter extends RecyclerView.Adapter<GeoFenceAdapter.ViewHolder>{

    List<GeoFenceEntity> geoFencesList;
    ItemListener itemListener;

    public GeoFenceAdapter(List<GeoFenceEntity> geoFencesList, ItemListener PlaceListen) {
        this.geoFencesList= geoFencesList;
        this.itemListener= PlaceListen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.geofence_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeoFenceEntity geoFence = geoFencesList.get(position);

        holder.nameTv.setText(geoFence.getName());
        holder.latTv.setText("lat: "+geoFence.getLat());
        holder.longTv.setText("long: "+geoFence.getLongt());

        holder.geoFenceCV.setOnClickListener(v->{ itemListener.placeShow(geoFencesList.get(position)); });
        holder.deleteBtn.setOnClickListener(v->{ itemListener.deletePlace(geoFencesList.get(position)); });
    }

    @Override
    public int getItemCount() {
        if(geoFencesList!=null)
            return geoFencesList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView geoFenceCV;
        MaterialButton deleteBtn;
        MaterialTextView nameTv,latTv,longTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            geoFenceCV = itemView.findViewById(R.id.card_lay);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            latTv = itemView.findViewById(R.id.lat_tv);
            longTv = itemView.findViewById(R.id.long_tv);
            nameTv = itemView.findViewById(R.id.name_tv);
        }
    }

    public void refresh(List<GeoFenceEntity> list){
        geoFencesList = list;
        notifyDataSetChanged();
    }

    public interface ItemListener{
        void deletePlace(GeoFenceEntity place);
        void placeShow(GeoFenceEntity place);
    }
}