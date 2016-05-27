package jiaqi.yanb.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiaqi.yanb.Note;
import jiaqi.yanb.R;
import jiaqi.yanb.ui.dialogs.SaveNoteDialog;

public class EditNoteActivity extends AppCompatActivity implements SaveNoteDialog.SaveNoteDialogCallback {

    public static final String EDIT_MODE = "edit_mode";



    public enum EditMode {
        CREATE, MODIFY;
    }

    private Note mNote;

    private EditMode mMode;

    @BindView(R.id.et_title)
    EditText mEtTitle;

    @BindView(R.id.et_note)
    EditText mEtNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ButterKnife.bind(this);


        mMode = (EditMode) getIntent().getSerializableExtra(EDIT_MODE);

        if (mMode == EditMode.CREATE) {
            mMode = EditMode.CREATE;
        } else if (mMode == EditMode.MODIFY) {
            mMode = EditMode.MODIFY;
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EditMode.CREATE) {
            if (isTitleEmpty() && isContentEmpty()) {
                Toast.makeText(this, R.string.edit_note_activity_discard_empty_note, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                FragmentManager fm = getSupportFragmentManager();
                // the dialog uses THIS activity as the callback listener to handle pos/neg event.
                SaveNoteDialog saveNoteDialog = new SaveNoteDialog();
                saveNoteDialog.show(fm, "save_note_dialog");
            }
        } else if (mMode == EditMode.MODIFY) {
            //TODO
        }
    }

    private boolean isContentEmpty() {
        return mEtNote.getText().toString().isEmpty();
    }

    private boolean isTitleEmpty() {
        return mEtTitle.getText().toString().isEmpty();
    }

    @Override
    public void onPositiveButtonClicked() {
        Toast.makeText(this, R.string.edit_note_activity_saving_note, Toast.LENGTH_SHORT).show();
        mNote = new Note(
                UUID.randomUUID(),
                mEtTitle.getText().toString(),
                mEtNote.getText().toString(),
                new Date());

        Intent intent = new Intent();
        intent.putExtra(MainActivity.NOTE, mNote);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {
        Toast.makeText(this, R.string.edit_note_activity_discard_non_empty_note, Toast.LENGTH_SHORT).show();
        finish();
    }
}
