package com.bpal.myapplication.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bpal.myapplication.DBModel.User;
import com.bpal.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forget extends AppCompatActivity {

    EditText etph, edtcode, cnfpass;
    Button btnSubmit, btncnf;
    TextView tvpass;
    ProgressDialog progressDialog;
    ConstraintLayout phone, pass;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        etph = findViewById(R.id.etEmail);
        tvpass = findViewById(R.id.tvpass);
        edtcode = findViewById(R.id.etcode);
        btncnf = findViewById(R.id.btnok);
        btnSubmit = findViewById(R.id.btnSubmit);

        phone = findViewById(R.id.confirm);
        pass = findViewById(R.id.password);

        reference = FirebaseDatabase.getInstance().getReference("User");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        phone.setVisibility(View.VISIBLE);
        pass.setVisibility(View.INVISIBLE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ph = etph.getText().toString().trim();
                final String code = edtcode.getText().toString().trim();

                if (ph.isEmpty()) {
                    etph.setError("Required");
                    etph.requestFocus();
                    return;
                }

                if (ph.length() != 10) {
                    etph.setError("Entet valid Number!");
                    etph.requestFocus();
                    return;
                }

                if (code.isEmpty()) {
                    edtcode.setError("Required");
                    edtcode.requestFocus();
                    return;
                }

                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(ph).exists()) {
                            progressDialog.dismiss();
                            User user = dataSnapshot.child(ph).getValue(User.class);
                            if (user.getPhone().equals(ph)) {
                                if (user.getCode().equals(code)) {
                                    Toast.makeText(getApplicationContext(), "Verification Completed", Toast.LENGTH_LONG).show();
                                    phone.setVisibility(View.INVISIBLE);
                                    pass.setVisibility(View.VISIBLE);
                                    //password();
                                    tvpass.setText(user.getPassword());
                                    btncnf.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(getApplicationContext(), LogIn.class));
                                            finish();
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Entered code is invalid!", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Entered number doesn't exists!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "User not exists!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }


}
