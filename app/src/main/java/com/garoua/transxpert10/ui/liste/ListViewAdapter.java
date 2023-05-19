package com.garoua.transxpert10.ui.liste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.garoua.transxpert10.R;
import com.garoua.transxpert10.trans_item;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.view.View;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter {

    public ArrayList<trans_item> trans_itemArrayList;

    public ListViewAdapter(Context context, ArrayList<trans_item> trans_itemArrayList){
        super(context, 0, trans_itemArrayList);
        this.trans_itemArrayList = trans_itemArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trans_item, parent, false
            );
        }

        TextView title = convertView.findViewById(R.id.text_title);
        TextView region = convertView.findViewById(R.id.text_region);
        ImageView imageView = convertView.findViewById(R.id.image_transfo);

        trans_item currentTrans = (trans_item) getItem(position);


        Glide.with(getContext())
                .load(currentTrans.getImage())
                .centerCrop()
                .into(imageView); // The imageView to load into
        title.setText(currentTrans.getDesignation());
        region.setText(currentTrans.getRegion());


        return convertView;
    }
}
