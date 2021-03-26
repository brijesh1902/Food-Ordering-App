package com.bpal.myapplication.Order;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.FavActivity;
import com.bpal.myapplication.Login.LogIn;
import com.bpal.myapplication.MainActivity;
import com.bpal.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class PaymentActivity extends AppCompatActivity {


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        new Database(getBaseContext()).clearCart(Common.currentuser.getPhone());

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
            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.order) {
            startActivity(new Intent(PaymentActivity.this, OrderActivity.class));
            finish();
        } else if (id == R.id.addhome) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
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
            startActivity(new Intent(PaymentActivity.this, FavActivity.class));
        }
        else if (id == R.id.logout) {
            Paper.book().destroy();
            auth.signOut();
            startActivity(new Intent(PaymentActivity.this, LogIn.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
