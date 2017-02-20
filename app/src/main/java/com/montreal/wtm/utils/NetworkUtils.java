package com.montreal.wtm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.montreal.wtm.R;

import java.net.HttpURLConnection;

import retrofit.RetrofitError;


public class NetworkUtils {


    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public static boolean isNetworkAvailableByWifi(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    public static int getNetworkConnectedType(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static String getAuthorizationBase64(String user, String password) {
        String credentials = user + ":" + password;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //
    // // NETWORK ERROR
    // //
    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean is401HTTPError(RetrofitError e) {

        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    public static boolean is403HTTPError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_FORBIDDEN);
    }

    public static boolean is404HTTPError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_NOT_FOUND);
    }

    public static boolean is406HTTPError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                & e.getResponse().getStatus() == HttpURLConnection.HTTP_NOT_ACCEPTABLE);
    }

    public static boolean is408HTTPError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }


    public static boolean is409HTTPError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_CONFLICT);
    }

    public static boolean is500HttpError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    public static boolean is503HttpError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_UNAVAILABLE);
    }

    public static boolean is422HttpError(RetrofitError e) {
        return (e.getKind() == RetrofitError.Kind.HTTP
                && e.getResponse().getStatus() == HttpURLConnection.HTTP_PRECON_FAILED);
    }

    public static boolean isAccessException(RetrofitError e) {
        return e.getKind() == RetrofitError.Kind.NETWORK;
    }


    public static String getMessageError(Context context, RetrofitError e) {
        if (is404HTTPError(e)) {
            //We can change the text to put a more personalize text
            return context.getString(R.string.error_message_serveur_prob);
        } else if (isAccessException(e)) {
            return context.getString(R.string.error_message_serveur_prob);
        }
        return context.getString(R.string.default_error_message);
    }


}
