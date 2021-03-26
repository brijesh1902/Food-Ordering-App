package com.bpal.myapplication.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bpal.myapplication.DBModel.User;
import com.bpal.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    TextView txtreg;
    Button register;
    EditText name, phone, email, password, age, etcode;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    ImageView Show;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        Show = findViewById(R.id.show);
        name = findViewById(R.id.editText);
        phone = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        password = findViewById(R.id.editText4);
        age = findViewById(R.id.age);
        etcode = findViewById(R.id.code);

        progressDialog = new ProgressDialog(this);

        txtreg = findViewById(R.id.registered);
        txtreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });

        register = findViewById(R.id.btnreg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name = name.getText().toString().trim();
                final String Phone = phone.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                final String Pass = password.getText().toString().trim();
                final String Age = age.getText().toString().trim();
                final String code = etcode.getText().toString().trim();

                if (Name.isEmpty()) {
                    name.setError("Manadatory!");
                    name.requestFocus();
                    return;
                }
                if (Pass.isEmpty()) {
                    password.setError("Manadatory!");
                    password.requestFocus();
                    return;
                }
                if (Phone.isEmpty()) {
                    phone.setError("Manadatory!");
                    phone.requestFocus();
                    return;
                }
                if (Age.isEmpty()) {
                    age.setError("Manadatory!");
                    age.requestFocus();
                    return;
                }
                if (Pass.length() <= 8) {
                    password.setError("Enter valid Password!");
                    password.requestFocus();
                    return;
                }
                if (Age.length() != 2) {
                    age.setError("Enter valid Age!");
                    age.requestFocus();
                    return;
                }
                if (Phone.length() != 10) {
                    phone.setError("Enter valid Number!");
                    phone.requestFocus();
                    return;
                }
                if (code.isEmpty()) {
                    etcode.setError("Required! 6 digit number required!");
                    etcode.requestFocus();
                    return;
                }
                if (code.length() <= 5) {
                    etcode.setError("Entet valid Code! 6 digit number required!");
                    etcode.requestFocus();
                    return;
                }

                final String Uphone = "+91"+Phone;

                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                final User user = new User(
                        Name,
                        Email,
                        Phone,
                        Pass,
                        Age,
                        code,
                        null
                );
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(Phone).exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Phone Number already exists!!", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child(Phone).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUp.this, LogIn.class));
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                progressDialog.dismiss();
            }
        });
    }


    public void showpass(View view) {
        if (view.getId() == R.id.show) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                Show.setImageResource(R.drawable.hidepass);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                Show.setImageResource(R.drawable.showpass);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null) {
            Intent intent = new Intent(this, PhoneAuth.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }*/
}
