package com.bpal.myapplication.Order;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.Adapters.CartAdapter;
import com.bpal.myapplication.Common.Common;
import com.bpal.myapplication.DBModel.Order;
import com.bpal.myapplication.DBModel.Request;
import com.bpal.myapplication.Database.Database;
import com.bpal.myapplication.R;
import com.bpal.myapplication.Service.APIService;
import com.bpal.myapplication.Service.MyResponse;
import com.bpal.myapplication.Service.Notification;
import com.bpal.myapplication.Service.Sender;
import com.bpal.myapplication.Service.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends Activity implements PaymentResultListener {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    RecyclerView recyclerView;
    TextView pname, price, pqty, totalprice;
    Button btnOrder;
    ImageView image;
    List<Order> list = new ArrayList<>();
    CartAdapter cartAdapter;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
    FirebaseAuth auth;
    String id = String.valueOf(System.currentTimeMillis());
    String fprice = "";
    APIService service;
    String Add1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        pname = findViewById(R.id.pdtname);
        price = findViewById(R.id.pdtprice);
        pqty = findViewById(R.id.pdtqty);
        image = findViewById(R.id.pdti);
        totalprice = findViewById(R.id.totalprice);
        btnOrder = findViewById(R.id.btnorder);
        recyclerView = findViewById(R.id.orderrv);

        auth = FirebaseAuth.getInstance();
        service = Common.getFCMService();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);

        loadlist();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your cart is empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    showAlertbox();
                }
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Delete")) {
            deletecart(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    private void deletecart(int order) {
        list.remove(order);
        new Database(this).clearCart(Common.currentuser.getPhone());
        for (Order item : list) {
            new Database(getBaseContext()).addtoCarts(item);
            loadlist();
        }
    }

    private void showAlertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.placeorder, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Checkout.preload(getApplicationContext());

        final EditText add = dialogView.findViewById(R.id.etdadd);
        Button btnsbmt = dialogView.findViewById(R.id.btnsbmt);
        Button btncan = dialogView.findViewById(R.id.buttonc);
        final RadioButton rdcod = dialogView.findViewById(R.id.rdcod);
        final RadioButton rdpaytm = dialogView.findViewById(R.id.rdpaytm);
        final RadioButton rdhome = dialogView.findViewById(R.id.rdhome);

        btncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        rdhome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   if (!TextUtils.isEmpty(Common.currentuser.getHomeaddress()) || Common.currentuser.getHomeaddress() != null) {
                       Add1 = Common.currentuser.getHomeaddress();
                       add.setText(Add1);
                   } else {
                       Toast.makeText(getApplicationContext(), "Please add Home Address first!!", Toast.LENGTH_SHORT).show();
                   }
                }
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

                Common.currentadd = Add;

                if (!rdcod.isChecked() && !rdpaytm.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please choose a payment option first!!", Toast.LENGTH_LONG).show();
                } else if (rdcod.isChecked()) {
                    Request req = new Request(
                            Common.currentuser.getPhone(),
                            Common.currentuser.getName(),
                            Add,
                            totalprice.getText().toString(),
                            list,
                            "COD",
                            "Unpaid",
                            id
                    );

                    reference.child(id).setValue(req);
                    new Database(getBaseContext()).clearCart(Common.currentuser.getPhone());
                    alertDialog.dismiss();
                    sendNotification(id);

                } else if (rdpaytm.isChecked()) {
                    if (ContextCompat.checkSelfPermission(CartActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.READ_SMS,
                                Manifest.permission.RECEIVE_SMS}, 101);
                    }

                    /*Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                    intent.putExtra("price", fprice);
                    intent.putExtra("list", (CharSequence) list);
                    startActivity(intent);*/

                    startPayment();
                    alertDialog.dismiss();
                }
            }

        });
    }

    private void sendNotification(final String ids) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data = databaseReference.orderByChild("isServertoken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Notification notification = new Notification("MyApp", "You have new order "+ids);
                    Sender content = new Sender(token.getToken(), notification);
                    service.sendNotification(content).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                    Toast.makeText(getApplicationContext(), "Thank You, Your Order is placed!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadlist() {
        list = new Database(getBaseContext()).getCarts(Common.currentuser.getPhone());
        cartAdapter = new CartAdapter(getApplicationContext(), list);
        cartAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartAdapter);
        int total = 0;
        for (Order order: list)
            total+=(Integer.parseInt(order.getPprice()))*(Integer.parseInt(order.getPquantity()));
        fprice = String.valueOf(total);
        Locale locale = new Locale("EN", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        totalprice.setText(fmt.format(total));
        Common.currentprice = fmt.format(total);
        Common.currentfoodlist = list;
    }

	public void startPayment() {
        final Activity activity = this;
        //Checkout.clearUserData(this);
        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_ZlvitOnKW93JOl");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyApp");
            options.put("description", "Reference No. #"+id);
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", R.drawable.chatkara);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("CURRENCY", "INR");
            double total = Double.parseDouble(String.valueOf(fprice));
            String tprice = String.valueOf(total*100);
            options.put("amount", tprice);
            options.put("payment_capture", false);

            JSONObject preFill = new JSONObject();
            preFill.put("email", Common.currentuser.getEmail());
            preFill.put("contact", Common.currentuser.getPhone());

            options.put("prefill", preFill);

            co.open(activity, options);
           /* razorpay.submit(co, new PaymentResultListener() {
                @Override
                public void onPaymentSuccess(String razorpayPaymentId) {
                    // Razorpay payment ID is passed here after a successful payment
                }

                @Override
                public void onPaymentError(int code, String description) {
                    // Error code and description is passed here
                }
            });
*/
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

            String id1 = String.valueOf(System.currentTimeMillis());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
            Request request = new Request(
                    Common.currentuser.getPhone(),
                    Common.currentuser.getName(),
                    Common.currentadd,
                    Common.currentprice,
                    Common.currentfoodlist,
                    "Razorpay",
                    "Paid",
                    id1
            );
            reference.child(id1).setValue(request);
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Thank You, Your Order is placed!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
            new Database(getBaseContext()).clearCart(Common.currentuser.getPhone());
            sendNotification(id1);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {

            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

}
