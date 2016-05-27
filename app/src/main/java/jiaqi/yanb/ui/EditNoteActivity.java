package jiaqi.yanb.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiaqi.yanb.Constants;
import jiaqi.yanb.Constants.EditMode;
import jiaqi.yanb.Note;
import jiaqi.yanb.R;
import jiaqi.yanb.ui.dialogs.SaveNoteDialog;

public class EditNoteActivity extends AppCompatActivity implements SaveNoteDialog.SaveNoteDialogCallback, TextWatcher {

    private Note mNote;

    private EditMode mEditMode;

    private boolean mNoteChanged = false;

    @BindView(R.id.et_title)
    EditText mEtTitle;

    @BindView(R.id.et_content)
    EditText mEtContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mEditMode = (EditMode) extras.getSerializable(Constants.EXTRA_KEY_NAME_EDIT_MODE);
            if (mEditMode == EditMode.MODIFY) {
                mNote = (Note) extras.getSerializable(Constants.EXTRA_KEY_NAME_NOTE);
                // if mEditMode == EditMode.MODIFY, mNote should not possibly be null.
                assert mNote != null;
                mEtTitle.setText(mNote.getTitle());
                mEtContent.setText(mNote.getContent());
            }
        }

        setTextChangedListeners();
    }

    private void setTextChangedListeners() {
        mEtTitle.addTextChangedListener(this);
        mEtContent.addTextChangedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mNoteChanged == false) {
            Toast.makeText(this, R.string.edit_note_activity_no_change, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (mEditMode == EditMode.CREATE) {
                if (isTitleEmpty() && isContentEmpty()) {
                    Toast.makeText(this, R.string.edit_note_activity_discard_empty_note, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    // the dialog uses THIS activity as the callback listener to handle pos/neg event.
                    SaveNoteDialog saveNoteDialog = new SaveNoteDialog();
                    saveNoteDialog.show(fm, "save_note_dialog");
                }
            } else if (mEditMode == EditMode.MODIFY) {
                if (isTitleEmpty()) {
                    Toast.makeText(this, R.string.edit_note_activity_title_cannot_be_empty, Toast.LENGTH_LONG).show();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    // the dialog uses THIS activity as the callback listener to handle pos/neg event.
                    SaveNoteDialog saveNoteDialog = new SaveNoteDialog();
                    saveNoteDialog.show(fm, "save_note_dialog");
                }
            }
        }
    }

    public boolean isNoteChanged() {
        return mNoteChanged;
    }

    public void noteChanged(boolean changed) {
        mNoteChanged = changed;
    }

    private boolean isContentEmpty() {
        return mEtContent.getText().toString().isEmpty();
    }

    private boolean isTitleEmpty() {
        return mEtTitle.getText().toString().isEmpty();
    }

    @Override
    public void onPositiveButtonClicked() {
        if (mEditMode == EditMode.CREATE) {
            Toast.makeText(this, R.string.edit_note_activity_saving_note, Toast.LENGTH_SHORT).show();
            mNote = new Note(
                    UUID.randomUUID(),
                    mEtTitle.getText().toString(),
                    mEtContent.getText().toString(),
                    new Date());

            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_KEY_NAME_NOTE, mNote);
            setResult(RESULT_OK, intent);
            finish();

        } else if (mEditMode == EditMode.MODIFY) {
            mNote.setTitle(mEtTitle.getText().toString());
            mNote.setContent(mEtContent.getText().toString());
            mNote.setDate(new Date());

            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_KEY_NAME_NOTE, mNote);
            setResult(RESULT_OK, intent);
            finish();

        } else {

        }
    }

    @Override
    public void onNegativeButtonClicked() {
        Toast.makeText(this, R.string.edit_note_activity_discard_non_empty_note, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mNoteChanged = true;
    }
}
