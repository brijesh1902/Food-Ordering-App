package com.bpal.myapplication.Order;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Adapters.OrderAdapter;
import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.Request;
import com.bpal.myapplication.FavActivity;
import com.bpal.myapplication.Login.LogIn;
import com.bpal.myapplication.MainActivity;
import com.bpal.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class OrderActivity extends AppCompatActivity {

    TextView id, price, status, phone, add;
    RecyclerView recyclerView;
    ArrayList<Request> list;
    DatabaseReference reference;
    OrderAdapter orderAdapter;
    FirebaseAuth auth;
    //FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        id = findViewById(R.id.odid);
        status = findViewById(R.id.odstatus);
        price = findViewById(R.id.odprice);
        phone = findViewById(R.id.odnum);
        add = findViewById(R.id.odadd);

        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.orderstatusrv);
        reference = FirebaseDatabase.getInstance().getReference("Order");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() == null) {
            loadOrders(Common.currentuser.getPhone());
        } else {
            loadOrders(getIntent().getStringExtra("phone"));
        }

    }

    private void loadOrders(String userphone) {
        //Log.e("Phone : ", userphone);
        Query queryRef = reference.orderByChild("userphone").equalTo(Common.currentuser.getPhone());
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list = new ArrayList<>();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Query id1 = snap.getRef().orderByChild("userphone").equalTo(Common.currentuser.getPhone());
                        Log.e("KEY : ", String.valueOf(snap.getRef()));
                        Request request = snap.getValue(Request.class);
                        list.add(request);


                    }
                    orderAdapter = new OrderAdapter(getApplicationContext(), list);
                    orderAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(orderAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.isChecked()) {
            return false;
        }
        if (id == R.id.home) {
            startActivity(new Intent(OrderActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.order) {
            startActivity(new Intent(OrderActivity.this, OrderActivity.class));
        } else if (id == R.id.addhome) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
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
        } else if (id == R.id.fav) {
            startActivity(new Intent(OrderActivity.this, FavActivity.class));
        }
        else if (id == R.id.logout) {
            Paper.book().destroy();
            auth.signOut();
            startActivity(new Intent(OrderActivity.this, LogIn.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

