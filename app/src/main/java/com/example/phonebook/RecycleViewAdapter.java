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

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements Filterable {

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
                Intent i = new Intent(v.getContext(),CallDetailsActivity.class);
                i.putExtra("name",m.getName());
                i.putExtra("contacts" ,m.getContact());
                v.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return model == null ? 0 : model.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Model> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(modelAll);
            }else{
                for(Model contactName : modelAll){
                    if(contactName.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(contactName);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            model.clear();
            modelAll.addAll((Collection<? extends Model>) results.values);
            notifyDataSetChanged();
        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, contact;
        ImageView callButton;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callButton = itemView.findViewById(R.id.callButton);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

    }

}
