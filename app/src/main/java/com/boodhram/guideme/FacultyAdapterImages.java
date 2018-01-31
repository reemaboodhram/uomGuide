package com.boodhram.guideme;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boodhram.guideme.Utils.ImageBuilding;

import java.util.ArrayList;
import java.util.List;


class FacultyAdapterImages extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    List<ImageBuilding> list;
    public FacultyAdapterImages(Context context) {
        this.mContext = context;
        this.list = populateList();
    }

    private List<ImageBuilding> populateList() {
        List<ImageBuilding> list = new ArrayList<>();
        Resources res = mContext.getResources();
        list.add(new ImageBuilding((R.drawable.a1),res.getString(R.string.faculty_label)));
        list.add(new ImageBuilding((R.drawable.a2),res.getString(R.string.faculty_flm)));
        list.add(new ImageBuilding((R.drawable.a3),res.getString(R.string.faculty_fos)));
        list.add(new ImageBuilding((R.drawable.a4),res.getString(R.string.faculty_foe)));
        list.add(new ImageBuilding((R.drawable.a5),res.getString(R.string.faculty_foa)));
        list.add(new ImageBuilding((R.drawable.a6),res.getString(R.string.faculty_fsssh)));
        list.add(new ImageBuilding((R.drawable.a7),res.getString(R.string.faculty_foicdt)));

        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_faculty, parent, false);
        return new FacultyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FacultyViewHolder view = (FacultyViewHolder) holder;
        ImageBuilding dto = list.get(position);
        view.img_faculty.setImageDrawable(ContextCompat.getDrawable(mContext,dto.getResourceId()));
        view.txt_faculty.setText(dto.getDescription());
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

    private class FacultyViewHolder extends FacultyAdapterImages.ViewHolder {
        ImageView img_faculty;
        TextView txt_faculty;
        FacultyViewHolder(View v) {
            super(v);
            img_faculty =  v.findViewById(R.id.img_faculty);
            txt_faculty =  v.findViewById(R.id.txt_faculty);


        }
    }
}
