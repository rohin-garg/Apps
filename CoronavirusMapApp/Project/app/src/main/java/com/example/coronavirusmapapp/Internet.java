package com.example.coronavirusmapapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Internet {
    private Context context;

    public Internet(Context context) {
        this.context = context;
    }

    public Boolean Check() {
        ConnectivityManager cn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection.!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}