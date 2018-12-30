package com.raptware.docdigitales;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.BranchItem> {

    private Context context;

    public BranchesAdapter(Context c){
        context=c;
    }
    @NonNull
    @Override
    public BranchItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_info, parent, false);
        BranchItem holder = new BranchItem(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BranchItem holder, int position) {
        try {
            JSONObject item=ManagementActivity.branches.getJSONObject(position);
            final int id=item.getInt("id");
            holder.tvSucursal.setText(item.getString("sucursal"));

            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BranchRegisterActivity.SetEditID(id);
                    Intent intent = new Intent(context,BranchRegisterActivity.class);
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ManagementActivity.branches.length();
    }

    public class BranchItem extends RecyclerView.ViewHolder{

        ImageView ivEdit;
        TextView tvSucursal;
        ConstraintLayout item;

        public BranchItem(View itemView) {
            super(itemView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvSucursal = itemView.findViewById(R.id.tvSucursal);
            item = itemView.findViewById(R.id.item);
        }
    }
}
