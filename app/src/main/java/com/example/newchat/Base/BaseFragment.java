package com.example.newchat.Base;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;




public class BaseFragment extends Fragment {
    public AlertDialog showMessage(String message, String posActionName){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();
    }
    public AlertDialog showMessage(int message, int posActionName){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();
    }

    public AlertDialog showMessage(String message, String posActionName,
                                   DialogInterface.OnClickListener onClickListener,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener );
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    public AlertDialog showMessage(String message, String posActionName,
                                   DialogInterface.OnClickListener onPosClick,
                                   String negativeText,
                                   DialogInterface.OnClickListener onNegativeClick,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onPosClick );
        builder.setNegativeButton(negativeText,onNegativeClick );
        builder.setCancelable(isCancelable);
        return builder.show();
    }

    public AlertDialog showMessage(String title,String message ,String posActionName,
                                   DialogInterface.OnClickListener onPosClick,
                                   String negativeText,
                                   DialogInterface.OnClickListener onNegativeClick,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onPosClick );
        builder.setNegativeButton(negativeText,onNegativeClick );
        builder.setCancelable(isCancelable);
        return builder.show();
    }


    public AlertDialog showMessage(int message, int posActionName,
                                   DialogInterface.OnClickListener onClickListener,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onClickListener);
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    public AlertDialog showMessage(int message, int posActionName,
                                   DialogInterface.OnClickListener onPosClick,
                                   int negativeText,
                                   DialogInterface.OnClickListener onNegativeClick,
                                   boolean isCancelable){
        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(posActionName,onPosClick );
        builder.setNegativeButton(negativeText,onNegativeClick );
        builder.setCancelable(isCancelable);
        return builder.show();
    }
    ProgressDialog dialog;
    public void showProgressDialog(String message){
        dialog =new ProgressDialog(getContext());
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void showProgressDialog(String title,String message , boolean isCancelable){
        dialog =new ProgressDialog(getContext());
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(isCancelable);
        dialog.show();
    }
    public void hideProgressDialog(){
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }
}
