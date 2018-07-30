package com.yesapp.yesapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

//for info pls check https://www.youtube.com/watch?v=ARezg1D9Zd0

public class ResetPassword extends AppCompatDialogFragment {

    private EditText resetPasswordEmail;
    private ResetPasswordListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ResetPasswordListener) context;
        } catch (ClassCastException e) {
        throw new ClassCastException(context.toString()+"must implement ResetPasswordListener ");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder DialogeBilder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view =layoutInflater.inflate(R.layout.reset_passowrd,null);
        DialogeBilder.setView(view)
        .setTitle("Reset Password").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        }).setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String resetPasswordEmailString = resetPasswordEmail.getText().toString().trim();
                listener.passTheResetEmail(resetPasswordEmailString);
            }
        });
        resetPasswordEmail = (EditText) view.findViewById(R.id.resetPasswordEmail);




return DialogeBilder.create();
    }
}
