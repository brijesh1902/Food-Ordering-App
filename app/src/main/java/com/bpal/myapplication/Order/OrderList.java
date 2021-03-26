package com.bpal.myapplication.Order;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Adapters.OrderListAdapter;
import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.Order;
import com.bpal.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderList extends AppCompatActivity {

    TextView id, name, ph, add, total, stat, pname, price, pqty, ordate, paymethod, paystat;
    ImageView image;
    RecyclerView recyclerView;
    OrderListAdapter orderListAdapter;
    DatabaseReference reference;
    ArrayList<Order> list;
    String key = "";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        auth = FirebaseAuth.getInstance();

        id=findViewById(R.id.ordid);
        name=findViewById(R.id.usname);
        ph=findViewById(R.id.usno);
        add=findViewById(R.id.usadd);
        total=findViewById(R.id.tprice);
        stat=findViewById(R.id.stat);
        ordate=findViewById(R.id.ordate);
        paymethod=findViewById(R.id.paymethod);
        paystat=findViewById(R.id.paystat);
        recyclerView=findViewById(R.id.listrv);

        image = findViewById(R.id.pdi);
        pname=findViewById(R.id.pdname);
        price=findViewById(R.id.pdprice);
        pqty=findViewById(R.id.pdqty);

        final String oid = getIntent().getExtras().getString("ordid");
        id.setText("Reference Id #"+oid);
        ordate.setText("Date: "+ Common.Time(Long.valueOf(oid)));

        final String pname = getIntent().getExtras().getString("uname");
        name.setText("Name: "+pname);

        final String no = getIntent().getExtras().getString("phone");
        ph.setText("Phone No.: "+no);

        final String oadd = getIntent().getExtras().getString("add");
        add.setText("Address: "+oadd);

        final String fprice = getIntent().getExtras().getString("price");
        total.setText("Total Amount: "+fprice);

        final String spaymethod = getIntent().getExtras().getString("paymethod");
        paymethod.setText("Payment Method: "+spaymethod);

        final String spaystat = getIntent().getExtras().getString("paystat");
        paystat.setText("Payment Status: "+spaystat);

        String ostat = getIntent().getExtras().getString("status");
        stat.setText("Delivery Status: "+ostat);

        key = getIntent().getExtras().getString("ordid");

        reference = FirebaseDatabase.getInstance().getReference("Order").child(key).child("foods");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderListAdapter);

        if (reference!=null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            list.add(snap.getValue(Order.class));
                        }
                        orderListAdapter = new OrderListAdapter(getApplicationContext(), list);
                        orderListAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(orderListAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
