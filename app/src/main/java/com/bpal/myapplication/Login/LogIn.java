package com.bpal.myapplication.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.User;
import com.bpal.myapplication.MainActivity;
import com.bpal.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    EditText txt1, txt2;
    Button sin;
    ImageView Pass1;
    TextView fopass, signup;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        txt1 = findViewById(R.id.email);
        txt2 = findViewById(R.id.pass);
        sin = findViewById(R.id.login);
        fopass = findViewById(R.id.fpass);
        Pass1 = findViewById(R.id.show1);
        signup = findViewById(R.id.signup);

        auth = FirebaseAuth.getInstance();

        Paper.init(this);

        progressDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference("User");

        fopass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogIn.this, Forget.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogIn.this, SignUp.class);
                startActivity(i);
            }
        });

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedtoInternet(getBaseContext())) {

                final String uname = txt1.getText().toString();
                final String pass = txt2.getText().toString();

                if (uname.isEmpty()) {
                    txt1.setError("Required!");
                    txt1.requestFocus();
                    return;
                }
                if (uname.length() != 10) {
                    txt1.setError("Enter valid Number!");
                    txt1.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    txt2.setError("Required!");
                    txt2.requestFocus();
                    return;
                }
                if (pass.length() <= 8) {
                    txt2.setError("Enter valid Password!");
                    txt2.requestFocus();
                    return;
                }

                   progressDialog.setMessage("Logging In...");
                   progressDialog.show();
                   reference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.child(uname).exists()) {
                               progressDialog.dismiss();
                               User user = dataSnapshot.child(uname).getValue(User.class);
                               if (user.getPhone().equals(uname) && user.getPassword().equals(pass)) {
                                   progressDialog.dismiss();
                                   Common.currentuser = user;
                                   Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_LONG).show();
                                   startActivity(new Intent(LogIn.this, MainActivity.class));
                                   finish();
                               } else {
                                   progressDialog.dismiss();
                                   Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                               }
                           }else {
                               progressDialog.dismiss();
                               Toast.makeText(getApplicationContext(), "User not exists!", Toast.LENGTH_LONG).show();
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               } else {
                    progressDialog.dismiss();
                   Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                   return;
               }

            }
        });
    }

    public void showpass1(View view) {
        if (view.getId()==R.id.show1) {
            if(txt2.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                Pass1.setImageResource(R.drawable.hidepass);
                txt2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                Pass1.setImageResource(R.drawable.showpass);
                txt2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}
