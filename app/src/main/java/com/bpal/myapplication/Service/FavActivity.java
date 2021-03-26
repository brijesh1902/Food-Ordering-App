package com.bpal.myapplication.Service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Adapters.FavFoodAdapter;
import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.Favorite;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.Order.CartActivity;
import com.bpal.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView name, price;
    ImageView image, fav;
    List<Favorite> list = new ArrayList<>();
    FavFoodAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        image = findViewById(R.id.itemimage);
        name = findViewById(R.id.itemname);
        price = findViewById(R.id.itemprice);
        fav = findViewById(R.id.favrorite);

        recyclerView = findViewById(R.id.favrv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favoriteAdapter);

        //list = new Database(this).getallfav(Common.currentuser.getPhone());
        favoriteAdapter = new FavFoodAdapter(this, new Database(this).getallfavfood(Common.currentuser.getPhone()));
        favoriteAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(favoriteAdapter);


        FloatingActionButton floatingActionButton = findViewById(R.id.cart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavActivity.this, CartActivity.class));
            }
        });


    }
}
