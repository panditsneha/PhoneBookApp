package com.example.phonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FavAdapter extends ArrayAdapter<FavModel> {
    public FavAdapter(@NonNull Context context, ArrayList<FavModel> favModels) {
        super(context,0,favModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View optionsItemView = convertView;
        if(optionsItemView == null){
            optionsItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_option_items,parent,false);
        }
        FavModel favModel = getItem(position);
        TextView name = optionsItemView.findViewById(R.id.Name);
        TextView number = optionsItemView.findViewById(R.id.Number);
        ImageView contactImage = optionsItemView.findViewById(R.id.contactImage);

        name.setText(favModel.getName());
        number.setText(favModel.getNumber());
        return optionsItemView;
    }

}
