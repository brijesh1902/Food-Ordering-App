package com.bpal.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Common.ItemClickListener;
import com.bpal.myapplication.DBModel.Request;
import com.bpal.myapplication.Order.OrderList;
import com.bpal.myapplication.R;

import java.util.ArrayList;

import static com.bpal.myapplication.Common.Common.convertcodetostatus;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Request> mdata;

    public OrderAdapter(Context context, ArrayList<Request> data) {
        mcontext = context;
        mdata =  data;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.orderstatuslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        final Request options = mdata.get(position);

        holder.id.setText("# "+options.getId());
        holder.add.setText(options.getAddress());
        holder.status.setText(convertcodetostatus(options.getStatus()));
        holder.phone.setText(options.getUserphone());
        holder.price.setText(options.getTotal());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent(mcontext, OrderList.class);
                intent.putExtra("ordid", options.getId());
                intent.putExtra("uname", options.getUsername());
                intent.putExtra("phone", options.getUserphone());
                intent.putExtra("add", options.getAddress());
                intent.putExtra("status", convertcodetostatus(options.getStatus()));
                intent.putExtra("price", options.getTotal());
                intent.putExtra("paymethod", options.getPaymentmethod());
                intent.putExtra("paystat", options.getPaymentstate());
                mcontext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id, price, status, phone, add;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.odid);
            status = itemView.findViewById(R.id.odstatus);
            price = itemView.findViewById(R.id.odprice);
            phone = itemView.findViewById(R.id.odnum);
            add = itemView.findViewById(R.id.odadd);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClickListener(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

    }
}