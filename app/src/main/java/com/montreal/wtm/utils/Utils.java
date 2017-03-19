package com.montreal.wtm.utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;

import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.EditText;

import com.montreal.wtm.R;
import com.montreal.wtm.utils.view.ViewUtils;


import java.io.File;
import java.util.Locale;


public class Utils {

    /**
     * To change fragment
     *
     * @param activity
     * @param idView
     * @param fragment
     */
    public static void changeFragment(FragmentActivity activity, int idView, android.support.v4.app.Fragment fragment) {
        if (!fragment.isAdded()) {
            activity.getSupportFragmentManager().beginTransaction().replace(idView, fragment).commit();
        }
    }


    public static void changeFragment(Activity activity, int idView, android.app.Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().replace(idView, fragment).commit();
        }
    }

    public static boolean checkIsEditTextEmpty(Activity activity, EditText editText) {
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setError(activity.getString(R.string.error_field_required));
            editText.requestFocus();
            return true;
        }
        return false;
    }



    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //
    // // JAIL BREAK
    // //
    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * JailBreak check
     *
     * @param binaryName
     * @return
     */
    private static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
                    "/data/local/xbin/", "/data/local/bin/",
                    "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    /**
     * Check JailBreak and return true if the phone is root
     *
     * @return
     */
    public static boolean isRooted() {
        return findBinary("su");
    }


    public static String getCountryCode(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        if (countryCode.equals("")) {
            return Locale.getDefault().getCountry();
        }
        return countryCode;
    }


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    /**
     * Show a alert message to insist or force the user to update the application
     *
     * @param context
     * @param forceUpdate
     */
    public static void updateTheApplication(final Activity context, boolean forceUpdate) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        if (forceUpdate) {
            ViewUtils.showAlertMessageWithCancel(context, context.getString(R.string.update_alert_force_title), context.getString(R.string.update_alert_force_message), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    context.finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.finish();
                }
            }, context.getString(R.string.update_alert_force_cancel_button), context.getString(R.string.update_alert_force_ok_button));
        } else {
            ViewUtils.showAlertMessageWithCancel(context, context.getString(R.string.update_alert_title), context.getString(R.string.update_alert_message), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }, null, context.getString(R.string.update_alert_cancel_button), context.getString(R.string.update_alert_ok_button));
        }
    }

}
