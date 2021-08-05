package com.example.phonebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class FragmentFav extends Fragment {

    GridView optionsGridView;

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fav_fragment,container,false);
        optionsGridView = v.findViewById(R.id.grid_view);

        ArrayList<FavModel> favModels = new ArrayList<>();

        //  yeh dono strings null hai
        String name = getArguments().getString("con_name").trim();
        String number = getArguments().getString("con_number");

        favModels.add(new FavModel(name,number));

        FavAdapter adapterOption = new FavAdapter(getContext(),favModels);
        optionsGridView.setAdapter(adapterOption);

        return v;
    }
}
