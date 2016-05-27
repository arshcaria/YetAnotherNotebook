package jiaqi.yanb.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiaqi.yanb.Constants;
import jiaqi.yanb.Note;
import jiaqi.yanb.NoteLab;
import jiaqi.yanb.R;
import jiaqi.yanb.adapters.RvNotesAdapter;
import jiaqi.yanb.backup.BackupRestore;
import jiaqi.yanb.ui.dialogs.BackupDialog;

public class MainActivity extends AppCompatActivity {

    private NoteLab mNoteLab;
    private List<Note> mNotes;
    private RecyclerView.LayoutManager mLayoutManager;
    private RvNotesAdapter mAdapter;
    private Handler mHandler;

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.rv_notes)
    RecyclerView mRvNotes;
    @BindView(R.id.fab_add_note)
    FloatingActionButton mFabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNoteLab = NoteLab.getInstance(this);
        mNotes = mNoteLab.getNotes();
        mHandler = new Handler(getMainLooper());

        initToolbar();
        initRecyclerView();
        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // backup notes
            case R.id.menu_item_backup: {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        showBackupDialog();
                    } else {
                        requestPermissions(
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constants.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    showBackupDialog();
                }

                return true;
            }
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*
                     * There is a bug in Android 6.0, which throws an exception
                     * (java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState)
                     * when showing a DialogFragment within onRequestPermissionsResult().
                     * The temporary workaround is to use postDelayed() to show the DialogFragment.
                     * You can use a delay of 0 as the parameter.
                     * The bug and discussion about it can be found in:
                     * https://code.google.com/p/android/issues/detail?id=196178
                     * http://stackoverflow.com/questions/33264031/calling-dialogfragments-show-from-within-onrequestpermissionsresult-causes
                     */
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showBackupDialog();
                        }
                    },0);
                } else {
                    Toast.makeText(this, R.string.main_activity_backup_fail_permission_denied, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ACTIVITY_REQUEST_CODE_CREATE: {
                    Note note = (Note) data.getSerializableExtra(Constants.EXTRA_KEY_NAME_NOTE);
                    mNoteLab.addNote(note);
                    updateUI();
                    break;
                }
                case Constants.ACTIVITY_REQUEST_CODE_MODIFY: {
                    Note note = (Note) data.getSerializableExtra(Constants.EXTRA_KEY_NAME_NOTE);
                    mNoteLab.updateNote(note);
                    updateUI();
                    break;
                }
            }
        } else {
//            Toast.makeText(MainActivity.this, "Something wrong with the returning result", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * obtain the latest list of notes and tell the adapter to refresh the recycler view.
     */
    private void updateUI() {
        mNotes = mNoteLab.getNotes();
        mAdapter.setNotes(mNotes);
        mAdapter.notifyDataSetChanged();
    }

    private void initListeners() {
        mFabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_NAME_EDIT_MODE, Constants.EditMode.CREATE);
                startActivityForResult(intent, Constants.ACTIVITY_REQUEST_CODE_CREATE);
            }
        });
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRvNotes.setLayoutManager(mLayoutManager);
        mAdapter = new RvNotesAdapter(mNotes, this);
        mRvNotes.setAdapter(mAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
    }

    private void showBackupDialog() {
        FragmentManager fm = getSupportFragmentManager();
        BackupDialog backupDialog = new BackupDialog();
        backupDialog.show(fm, "backup_dialog");
    }
}
