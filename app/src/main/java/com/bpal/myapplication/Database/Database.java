package com.bpal.myapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.bpal.myapplication.DBModel.Favorite;
import com.bpal.myapplication.DBModel.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "DApp.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(String phone){
        SQLiteDatabase db= getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect = {"phone", "pname", "pquantity", "pprice", "pdiscount", "pimage"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlselect, "phone=?", new String[] {phone}, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("phone")),
                        c.getString(c.getColumnIndex("pname")),
                        c.getString(c.getColumnIndex("pquantity")),
                        c.getString(c.getColumnIndex("pprice")),
                        c.getString(c.getColumnIndex("pdiscount")),
                        c.getString(c.getColumnIndex("pimage"))
                        ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addtoCarts(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String squery = String.format("INSERT OR REPLACE INTO OrderDetail(phone, pname, pquantity, pprice, pdiscount, pimage) " +
                        "VALUES('%s','%s','%s','%s','%s', '%s');",
                order.getPhone(), order.getPname(), order.getPquantity(), order.getPprice(), order.getPdiscount(), order.getPimage());
        db.execSQL(squery);
    }

    public void clearCart(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE phone='%s'", phone);
        db.execSQL(query);
    }

    public void updateCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET pquantity='%s' WHERE phone='%s' AND id='%s'", order.getPquantity(), order.getPhone());
        db.execSQL(query);
    }

    public boolean existsfood(String phone) {
        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String query = String.format("SELECT * From OrderDetail WHERE phone='%s'", phone);
        cursor = db.rawQuery(query, null);
        if (cursor.getCount()>0) {
            flag=true;
        } else {
            flag=false;
        }
        cursor.close();
        return flag;
    }

    public void increaseCart(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET pquantity= pquantity+1 WHERE phone='%s'", phone);
        db.execSQL(query);
    }

    ///////////////////////////////////////////////////////////////////////////

    public boolean isfavfood(String id, String userphone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM FavFood WHERE id='%s' and userphone='%s';", id, userphone);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void addtofavfood(Favorite food) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO FavFood(" + "id, fname, fprice, fdiscount, fdesc, fimage, userphone) " +
                "VALUES('%s','%s','%s','%s', '%s', '%s', '%s');", food.getId(), food.getFname(), food.getFprice(),
                food.getFdiscount(), food.getFdescription(), food.getPic(), food.getUserphone());
        db.execSQL(query);
    }

    public void removefromfavfood(String id, String userphone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM FavFood WHERE id='%s' and userphone='%s';", id, userphone);
        db.execSQL(query);
    }

    public List<Favorite> getallfavfood(String userphone){
        SQLiteDatabase db= getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect = {"id", "fname", "fprice", "fdiscount", "fdesc", "fimage", "userphone"};
        String sqlTable = "FavFood";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlselect, "userphone=?", new String[] {userphone}, null, null, null);

        final List<Favorite> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Favorite(
                        c.getString(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("fname")),
                        c.getString(c.getColumnIndex("fprice")),
                        c.getString(c.getColumnIndex("fdiscount")),
                        c.getString(c.getColumnIndex("fdesc")),
                        c.getString(c.getColumnIndex("fimage")),
                        c.getString(c.getColumnIndex("userphone"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

}
