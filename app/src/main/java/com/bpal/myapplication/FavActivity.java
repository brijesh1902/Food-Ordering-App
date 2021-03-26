package com.bpal.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Adapters.FavFoodAdapter;
import com.bpal.myapplication.Adapters.FoodAdapter;
import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.DBFood;
import com.bpal.myapplication.DBModel.Favorite;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.Login.LogIn;
import com.bpal.myapplication.Order.CartActivity;
import com.bpal.myapplication.Order.OrderActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView name, price;
    ImageView image, fav;
    List<Favorite> list = new ArrayList<>();
    FavFoodAdapter favoriteAdapter;
    FirebaseAuth auth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchview);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        return true;
    }

    private void search(String newText) {
        List<Favorite> mylist = new ArrayList<>();
        for (Favorite dbFood:list) {
            if ( dbFood.getFname().toLowerCase().contains(newText.toLowerCase())
                    || dbFood.getFprice().toLowerCase().contains(newText.toLowerCase())) {
                mylist.add(dbFood);
            } else {
                Toast.makeText(getApplicationContext(), "Data not available!!", Toast.LENGTH_LONG).show();
            }
        }
        favoriteAdapter = new FavFoodAdapter(getApplicationContext(), mylist);
        recyclerView.setAdapter(favoriteAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.searchview) {
            return true;
        }
        else  if (id == R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        if (id == R.id.order) {
            startActivity(new Intent(getApplicationContext(), OrderActivity.class));
        }
        else if (id == R.id.addhome) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FavActivity.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View dialogView = layoutInflater.inflate(R.layout.addhome, viewGroup, false);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            final EditText add = dialogView.findViewById(R.id.homeadd);
            Button btnsbmt = dialogView.findViewById(R.id.buttonyes);
            Button btncan = dialogView.findViewById(R.id.buttonno);

            btncan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            btnsbmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Add = add.getText().toString().trim();
                    if (Add.isEmpty()) {
                        add.setError("Address Required!");
                        return;
                    }

                    Common.currentuser.setHomeaddress(Add);
                    FirebaseDatabase.getInstance().getReference("User").child(Common.currentuser.getPhone()).setValue(Common.currentuser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    alertDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Home Address Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

        }
        else if (id == R.id.fav) {
            startActivity(new Intent(FavActivity.this, FavActivity.class));
        }
        else if (id == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(FavActivity.this, LogIn.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        image = findViewById(R.id.itemimage);
        name = findViewById(R.id.itemname);
        price = findViewById(R.id.itemprice);
        fav = findViewById(R.id.favrorite);
        auth = FirebaseAuth.getInstance();

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
