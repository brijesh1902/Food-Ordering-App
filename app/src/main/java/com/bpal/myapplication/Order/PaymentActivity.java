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

	//String customerId = generateString();
    //String orderId = generateString();
    //String mid = "xdyMAt36873849388280";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();

     //   getCheckSum cs = new getCheckSum();
       // cs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	   
	   //Checkout.preload(getApplicationContext());

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
       /* if (toggle.onOptionsItemSelected(item)) {
            return true;
        }*/
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

/*
    public class getCheckSum extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(PaymentActivity.this);

        String url = "https://bpalztech.000webhostapp.com/payment/paytm/paytm/generateChecksum.php";
        String varifyurl = "https://pguat.paytm.in/paytmchecksum/paytmCallback.jsp";

        String CHECKSUMHASH = "";

        @Override
        protected void  onPreExecute() {
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
        }

        public final String doInBackground(ArrayList<String>... alldata) {

            JsonParser jsonParser = new JsonParser(PaymentActivity.this);
            String param = "MID="+mid+"&ORDER_ID="+orderId+"&CUST_ID="+customerId+"&CHANNEL_ID=WAP&TXN_AMOUNT="+amount+
                    "&WEBSITE=APP_STAGING" +"CALLBACK_URL="+varifyurl+"&INDUSTRY_TYPE_ID=Retail";
            Log.e("PoastData", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);

            if (jsonObject != null) {

                try {
                    CHECKSUMHASH = jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("Checksum result>>", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("setup acc", "signup result" +s);
            if (dialog.isShowing()) {
                this.dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getProductionService();
            
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put( "MID" , mid);
            paramMap.put( "ORDER_ID" , orderId);
            paramMap.put( "CUST_ID" , customerId);
          //  paramMap.put( "MOBILE_NO" , "7777777777");
          //  paramMap.put( "EMAIL" , "abc@gmail.com");
            paramMap.put( "CHANNEL_ID" , "WAP");
            paramMap.put( "TXN_AMOUNT" ,  amount);
            paramMap.put( "WEBSITE" , "WEBSTAGING");
            paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
            paramMap.put( "CALLBACK_URL", varifyurl);
            paramMap.put( "CHECKSUMHASH" , CHECKSUMHASH);

            PaytmOrder Order = new PaytmOrder(paramMap);


            PaytmMerchant Merchant = new PaytmMerchant(
                    "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                    "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

            Service.initialize(Order, null);

            //Service.initialize(Order, null);
            Service.startPaymentTransaction(PaymentActivity.this, true, true,
                    PaymentActivity.this);

            super.onPostExecute(s);
        }
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getApplicationContext(), "Network not available! Please try again later!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "Authentication failed!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "UI Error Occurred!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(getApplicationContext(), "Error Loading WebPage!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction Cancelled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(getApplicationContext(), "Transaction Cancel!!", Toast.LENGTH_LONG).show();
    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

*/
