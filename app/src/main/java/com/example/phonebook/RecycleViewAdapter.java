package com.example.phonebook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    //Activity activity;
    Context context;
    ArrayList<Model> model;
    ArrayList<Model> modelAll;

    public RecycleViewAdapter(Context context, ArrayList<Model> model) {
        //this.activity = activity;
        this.context=context;
        this.model = model;
        this.modelAll = new ArrayList<>(model);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        Model m = model.get(position);
        holder.name.setText(m.getName());
        holder.contact.setText(m.getContact());
        holder.id.setText(String.valueOf(m.getId()));
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(view.getContext(), "Please Grant Permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {

                    String s = "tel:" + holder.contact.getText();
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse(s));
                    view.getContext().startActivity(i);
                }
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model m = model.get(position);
                Intent i = new Intent(v.getContext(),CallDetailsActivity.class);
                i.putExtra("name",m.getName());
                i.putExtra("contacts" ,m.getContact());
                v.getContext().startActivity(i);
            }
        });
    }

    public void filterList(ArrayList<Model> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        model = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return model == null ? 0 : model.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, contact,id;
        ImageView callButton,contactImageView;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImageView=itemView.findViewById(R.id.image_view);
            callButton = itemView.findViewById(R.id.callButton);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            id=itemView.findViewById(R.id.ids);
        }

    }

}
