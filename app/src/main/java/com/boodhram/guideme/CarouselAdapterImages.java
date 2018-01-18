package com.boodhram.guideme;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boodhram.guideme.Utils.BuildingDTO;
import com.boodhram.guideme.Utils.UomService;
import com.boodhram.guideme.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaulkory on 12/28/2017.
 */

public class CarouselAdapterImages extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    List<ImageBuilding> list;
    public CarouselAdapterImages(Context context, BuildingDTO buildingDTO) {
        this.mContext = context;
        this.list = populateList(buildingDTO);
    }

    private List<ImageBuilding> populateList(BuildingDTO buildingDTO) {
        BuildingDTO dto = new UomService(mContext).getplaceById(buildingDTO.getId());
        List<ImageBuilding> list = Utils.getImageForBuilding(dto);
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_image, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder view = (ImageViewHolder) holder;
        ImageBuilding dto = list.get(position);
        view.image_premium.setImageDrawable(ContextCompat.getDrawable(mContext,dto.getResourceId()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    private class ImageViewHolder extends ViewHolder {
        ImageView image_premium;
        ImageViewHolder(View v) {
            super(v);
            image_premium =  v.findViewById(R.id.image_premium);


        }
    }
}
