package com.bpal.myapplication.Order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.Order;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.R;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView itemimage;
    TextView itemname, itemprice, itemdesc, vname;
    ElegantNumberButton numberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemimage = findViewById(R.id.itimage);
        itemname = findViewById(R.id.itname);
        itemdesc = findViewById(R.id.itemdesc);
        itemprice = findViewById(R.id.itprice);
        numberButton = findViewById(R.id.itqty);

        final String image = getIntent().getExtras().getString("itemimage");
        Glide.with(this).load(image).into(itemimage);

        final String pname = getIntent().getExtras().getString("itemname");
        itemname.setText(pname);

        final String fprice = getIntent().getExtras().getString("itemprice");
        itemprice.setText("₹ " + fprice);

        final String fdesc = getIntent().getExtras().getString("itemdesc");
        itemdesc.setText(fdesc);

        final String dis = getIntent().getExtras().getString("dis");

        FloatingActionButton floatingActionButton = findViewById(R.id.itcart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addtoCarts(new Order(
                        Common.currentuser.getPhone(),
                        pname,
                        numberButton.getNumber(),
                        fprice,
                        dis,
                        image
                ));
                //Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_LONG).show();
                Snackbar.make(v, "Added to Cart", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
}
