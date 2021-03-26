package com.bpal.myapplication.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.widget.EditText;

import com.bpal.myapplication.DBModel.DBFood;
import com.bpal.myapplication.DBModel.Order;
import com.bpal.myapplication.DBModel.User;
import com.bpal.myapplication.Service.APIService;
import com.bpal.myapplication.Service.RetrofitClient;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Common {

    public static User currentuser;
    public static DBFood currentfood;
    public static DBFood currentfavfood;

    public static String foodkey="";

    public static String currentadd;
    public static String currentprice;
    public static List<Order> currentfoodlist;

    public static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "password";

    public static String Time(Long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy",calendar).toString();
        return date;
    }

    public static boolean isConnectedtoInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info!=null){
                for (int i=0; i<info.length; i++) {
                    if (info[i].getState()== NetworkInfo.State.CONNECTED);
                    return true;
                }
            }
        }
        return false;
    }

    public static String convertcodetostatus(String status) {
        if (status.equals("0")) {
            return "Placed";
        } else if (status.equals("1")) {
            return "Shipped";
        } else if (status.equals("2")){
            return "On my way";
        } else {
            return "Delivered";
        }
    }

}
