package jiaqi.yanb.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import jiaqi.yanb.R;
import jiaqi.yanb.backup.BackupRestore;

/**
 * Created by Jiaqi on 5/27/2016.
 */
public class BackupDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(getString(R.string.backup_dialog_create_backup_warnning))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BackupRestore.getInstance(getActivity()).backupNotes();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), R.string.backup_dialog_backup_canceled, Toast.LENGTH_SHORT).show();
                    }
                });
        return dialogBuilder.create();
    }
}
