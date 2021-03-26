package com.bpal.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bpal.myapplication.Adapters.FoodAdapter;
import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.DBFood;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.Login.LogIn;
import com.bpal.myapplication.Order.CartActivity;
import com.bpal.myapplication.Order.OrderActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.grpc.okhttp.internal.framed.Header;

public class MainActivity extends AppCompatActivity {

    ArrayList<DBFood> list;
    RecyclerView foodrv;
    FoodAdapter foodAdapter;
    TextView name, price;
    ImageView image, fav;
    DatabaseReference reference;
    FirebaseAuth auth;
    Database database;

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
        ArrayList<DBFood> mylist = new ArrayList<>();
        for (DBFood dbFood:list) {
            if ( dbFood.getFname().toLowerCase().contains(newText.toLowerCase())
                    || dbFood.getFprice().toLowerCase().contains(newText.toLowerCase())) {
                mylist.add(dbFood);
            } else {
                Toast.makeText(getApplicationContext(), "Data not available!!", Toast.LENGTH_LONG).show();
            }
        }
        foodAdapter = new FoodAdapter(getApplicationContext(), mylist);
        foodrv.setAdapter(foodAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reference = FirebaseDatabase.getInstance().getReference("Category").child("Foods");
        auth = FirebaseAuth.getInstance();
        database = new Database(this);

        image = findViewById(R.id.itemimage);
        name = findViewById(R.id.itemname);
        price = findViewById(R.id.itemprice);
        fav = findViewById(R.id.favrorite);

        FloatingActionButton floatingActionButton = findViewById(R.id.cart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        foodrv = findViewById(R.id.alldishrv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        foodrv.setHasFixedSize(true);
        foodrv.setLayoutManager(layoutManager);
        foodrv.setAdapter(foodAdapter);

        if (reference!=null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            list.add(snap.getValue(DBFood.class));
                        }
                        foodAdapter = new FoodAdapter(getApplicationContext(), list);
                        foodrv.setAdapter(foodAdapter);
                        foodAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

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
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            startActivity(new Intent(MainActivity.this, FavActivity.class));
        }
        else if (id == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LogIn.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
