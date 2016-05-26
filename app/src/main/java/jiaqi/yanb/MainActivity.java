package jiaqi.yanb;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiaqi.yanb.adapters.RvNotesAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE = 0;
    private static final int MODIFY = 1;

    public static final String NOTE = "note";

    private List<Note> mNotes;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    //    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    //    @BindView(R.id.rv_notes)
    RecyclerView mRvNotes;
    //    @BindView(R.id.fab_add_note)
    FloatingActionButton mFabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);

        mNotes = new ArrayList<>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mRvNotes = (RecyclerView) findViewById(R.id.rv_notes);
        mFabAddNote = (FloatingActionButton) findViewById(R.id.fab_add_note);
        initToolbar();
        initRecyclerView();
        initListeners();
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
                    mNotes.add(note);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
                case MODIFY: {
                    Note note = (Note) data.getSerializableExtra(NOTE);
                    mNotes.add(note);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Something wrong with the returning result", Toast.LENGTH_SHORT).show();
        }
    }
}
