package jiaqi.yanb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNoteActivity extends AppCompatActivity {

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
            mNote = new Note(
                    UUID.randomUUID(),
                    mEtTitle.getText().toString(),
                    mEtNote.getText().toString(),
                    new Date());
        } else if (mMode == EditMode.MODIFY) {
            mNote.updateNote(
                    mEtTitle.getText().toString(),
                    mEtNote.getText().toString(),
                    new Date());

        }
        Intent intent = new Intent();
        intent.putExtra(MainActivity.NOTE, mNote);
        setResult(RESULT_OK, intent);
        finish();
    }
}
