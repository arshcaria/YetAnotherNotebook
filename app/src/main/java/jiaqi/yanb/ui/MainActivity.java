package jiaqi.yanb.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiaqi.yanb.Note;
import jiaqi.yanb.NoteLab;
import jiaqi.yanb.R;
import jiaqi.yanb.adapters.RvNotesAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE = 0;
    private static final int MODIFY = 1;

    public static final String NOTE = "note";

    private NoteLab mNoteLab;
    private List<Note> mNotes;
    private RecyclerView.LayoutManager mLayoutManager;
    private RvNotesAdapter mAdapter;

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

        initToolbar();
        initRecyclerView();
        initListeners();
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
                intent.putExtra(EditNoteActivity.EDIT_MODE, EditNoteActivity.EditMode.CREATE);
                startActivityForResult(intent, CREATE);
            }
        });
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRvNotes.setLayoutManager(mLayoutManager);
        mAdapter = new RvNotesAdapter(mNotes);
        mRvNotes.setAdapter(mAdapter);
    }

    private void initToolbar() {
        mToolbar.setTitle(R.string.app_name);

        mToolbar.inflateMenu(R.menu.menu_main);

        Menu toolbarMenu = mToolbar.getMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CREATE: {
                    Note note = (Note) data.getSerializableExtra(NOTE);
                    mNoteLab.addNote(note);
                    updateUI();
                    break;
                }
                case MODIFY: {
                    Note note = (Note) data.getSerializableExtra(NOTE);
                    mNoteLab.updateNote(note);
                    updateUI();
                    break;
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Something wrong with the returning result", Toast.LENGTH_SHORT).show();
        }
    }
}
