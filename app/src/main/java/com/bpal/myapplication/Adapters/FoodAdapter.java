package com.bpal.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.Common.ItemClickListener;
import com.bpal.myapplication.DBModel.DBFood;
import com.bpal.myapplication.DBModel.Favorite;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.Order.ItemDetailActivity;
import com.bpal.myapplication.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<DBFood> mdata;

    public FoodAdapter(Context context, ArrayList<DBFood> data) {
        mcontext = context;
        mdata = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.itemview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DBFood options = mdata.get(position);
        Database database = new Database(mcontext);

        if (database.isfavfood(String.valueOf(position), Common.currentuser.getPhone())) {
            holder.fav.setImageResource(R.drawable.favorite);
        } else {
            holder.fav.setImageResource(R.drawable.unfavorite);
        }

        Glide.with(mcontext).load(options.getPic()).into(holder.image);
        holder.name.setText(options.getFname());
        holder.price.setText("₹ " + options.getFprice());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent(mcontext, ItemDetailActivity.class);
                intent.putExtra("itemname", options.getFname());
                intent.putExtra("itemprice", options.getFprice());
                intent.putExtra("itemdesc", options.getFdescription());
                intent.putExtra("itemimage", options.getPic());
                intent.putExtra("dis", options.getFdiscount());
                mcontext.startActivity(intent);
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = new Database(mcontext);
                Favorite favorite = new Favorite();
                favorite.setId(String.valueOf(position));
                favorite.setFname(options.getFname());
                favorite.setFprice(options.getFprice());
                favorite.setFdescription(options.getFdescription());
                favorite.setFdiscount(options.getFdiscount());
                favorite.setPic(options.getPic());
                favorite.setUserphone(Common.currentuser.getPhone());

                if (!database.isfavfood(String.valueOf(position), Common.currentuser.getPhone())) {
                    database.addtofavfood(favorite);
                    holder.fav.setImageResource(R.drawable.favorite);
                    Toast.makeText(mcontext, options.getFname()+" added to favorite", Toast.LENGTH_LONG).show();

                } else {
                    database.removefromfavfood(String.valueOf(position), Common.currentuser.getPhone());
                    holder.fav.setImageResource(R.drawable.unfavorite);
                    Toast.makeText(mcontext, options.getFname()+" removed from favorite", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image, fav;
        TextView name, price;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemimage);
            name = itemView.findViewById(R.id.itemname);
            price = itemView.findViewById(R.id.itemprice);
            fav = itemView.findViewById(R.id.favrorite);
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
