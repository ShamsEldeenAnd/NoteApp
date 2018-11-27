package com.developer.shams.noteapp.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.developer.shams.noteapp.Adapter.NoteAdapter;
import com.developer.shams.noteapp.DataBase.Note;
import com.developer.shams.noteapp.R;
import com.developer.shams.noteapp.Utilities.Constants;
import com.developer.shams.noteapp.ViewModels.NoteViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListner {

    public final static int ADD_NOTE = 1;
    public final static int EDT_NOTE = 2;
    private RecyclerView notesList;
    private RecyclerView.LayoutManager manager;
    private FloatingActionButton button;
    private NoteAdapter adapter;
    private NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

        setTitle("Note");
        notesList = findViewById(R.id.notes);
        button = findViewById(R.id.add_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE);
            }
        });


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        initRecyclerview();
    }

    //init recycler view
    private void initRecyclerview() {

        //observe the change in room db
        noteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter = new NoteAdapter(notes, MainActivity.this);
                notesList.setAdapter(adapter);
            }
        });

        manager = new LinearLayoutManager(this);
        notesList.setHasFixedSize(true);
        notesList.setLayoutManager(manager);

        //add divider to recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        notesList.addItemDecoration(dividerItemDecoration);


        //add swipe touch action
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //delete on swipe
                noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted !", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(notesList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //in case of note addition
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            String titleval = data.getStringExtra(Constants.EXTRA_TITLE);
            String descval = data.getStringExtra(Constants.EXTRA_DESCRIPTION);
            int pri = data.getIntExtra(Constants.EXTRA_PRIORITY, 1);
            Note note = new Note(titleval, descval, pri);
            noteViewModel.insert(note);
        }
        //in case of note update
        else if (requestCode == EDT_NOTE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(Constants.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
            String titleval = data.getStringExtra(Constants.EXTRA_TITLE);
            String descval = data.getStringExtra(Constants.EXTRA_DESCRIPTION);
            int pri = data.getIntExtra(Constants.EXTRA_PRIORITY, 1);
            Note note = new Note(titleval, descval, pri);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    //handling click note item
    @Override
    public void OnItimClick(Note note) {
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        intent.putExtra(Constants.EXTRA_TITLE, note.getTitle());
        intent.putExtra(Constants.EXTRA_DESCRIPTION, note.getDescription());
        intent.putExtra(Constants.EXTRA_PRIORITY, note.getProirity());
        intent.putExtra(Constants.EXTRA_ID, note.getId());
        startActivityForResult(intent, EDT_NOTE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            noteViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
