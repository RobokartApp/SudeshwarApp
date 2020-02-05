package com.ark.robokart_robotics.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.ark.robokart_robotics.R;

public class AlertDialog {

    public void showDialog(Context context, String title, String message, int icon_enabled, String icon){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        if(icon_enabled == 1){
            switch(icon){
                case "info":
                    builder.setIcon(R.drawable.ic_info_outline_black_24dp);
                    break;

                default:
                    builder.setIcon(R.drawable.ic_info_outline_black_24dp);
                    break;
            }
        }

        String positiveText = "OK";

        builder.setPositiveButton(positiveText, (dialog, which) -> dialog.dismiss());

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }



    public void showDialogCloseActivity(Activity context, String title, String message){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        String positiveText = "OK";

        //builder.setPositiveButton(positiveText, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                context.finish();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

}
