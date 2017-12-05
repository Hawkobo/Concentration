package com.example.jason.ftp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class HighScoreDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstancedState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.play_dialog)
                .setItems(R.array.play_dialog_string_array, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String[] optionsArray = getResources().getStringArray(R.array.play_dialog_string_array);

                        Intent nextAct = new Intent(getActivity(), HSActivity.class);
                        nextAct.putExtra("numWords", Integer.parseInt(optionsArray[i]));
                        nextAct.putExtra("playingValue",getArguments().getBoolean("playingValue"));
                        startActivity(nextAct);
                    }
                })
                .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });

        return builder.create();
    }
}
