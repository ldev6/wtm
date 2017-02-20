package com.montreal.wtm.utils.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class ViewUtils {


    public static void showAlertMessage(Context context, String title, String message, DialogInterface.OnClickListener onClick) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNegativeButton("OK", onClick);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public static void showAlertMessageWithCancel(Context context, String title, String message, DialogInterface.OnClickListener onClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("OK", onClick);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public static void showAlertMessageWithCancel(Context context, String title, String message, DialogInterface.OnClickListener onClick, DialogInterface.OnClickListener onClickCancel, String cancelButtonText, String okButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);

        if(onClickCancel!=null) {
            builder.setNegativeButton(cancelButtonText, onClickCancel);
        }else {
            builder.setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        builder.setPositiveButton(okButtonText, onClick);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
