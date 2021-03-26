package com.bpal.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Common.ItemClickListener;
import com.bpal.myapplication.DBModel.Favorite;
import com.bpal.myapplication.Order.ItemDetailActivity;
import com.bpal.myapplication.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class FavFoodAdapter extends RecyclerView.Adapter<FavFoodAdapter.ViewHolder> {

    private Context mcontext;
    private List<Favorite> mdata;

    public FavFoodAdapter(Context context, List<Favorite> data) {
        mcontext = context;
        mdata = data;
    }

    @NonNull
    @Override
    public FavFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.favoritelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavFoodAdapter.ViewHolder holder, int position) {
        final Favorite options = mdata.get(position);

        Glide.with(mcontext).load(options.getPic()).into(holder.image);
        holder.name.setText(options.getFname());
        holder.price.setText("₹ "+options.getFprice());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent(mcontext, ItemDetailActivity.class);
                intent.putExtra("key", options.getId());
                intent.putExtra("itemname", options.getFname());
                intent.putExtra("itemprice", options.getFprice());
                intent.putExtra("itemimage", options.getPic());
                intent.putExtra("itemdesc", options.getFdescription());
                intent.putExtra("dis", options.getFdiscount());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name, price;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itmage);
            name = itemView.findViewById(R.id.itname);
            price = itemView.findViewById(R.id.itprice);

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