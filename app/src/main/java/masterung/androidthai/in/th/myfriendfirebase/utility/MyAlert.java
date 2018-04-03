package masterung.androidthai.in.th.myfriendfirebase.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import masterung.androidthai.in.th.myfriendfirebase.R;

public class MyAlert {
    private Context context;

    public MyAlert(Context context) {
        this.context = context;
    }

    public void myNormalDialog(String titleString, String messageString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.alert);
        builder.setTitle(titleString);
        builder.setMessage(messageString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
