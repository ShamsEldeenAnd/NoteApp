package com.developer.shams.noteapp.UI;

import android.content.Intent;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.developer.shams.noteapp.R;
import com.developer.shams.noteapp.Utilities.Constants;


public class AddNoteActivity extends AppCompatActivity {


    private EmojiEditText title, description, priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Add Note");

        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);
        priority = findViewById(R.id.priority);
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.EXTRA_ID)) {
            priority.setText(String.valueOf(intent.getIntExtra(Constants.EXTRA_PRIORITY, 1)));
            title.setText(intent.getStringExtra(Constants.EXTRA_TITLE));
            description.setText(intent.getStringExtra(Constants.EXTRA_DESCRIPTION));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String tileval = title.getText().toString();
        String desc = description.getText().toString();
        int priorityval = Integer.parseInt(priority.getText().toString());
        Intent data = new Intent();
        data.putExtra(Constants.EXTRA_TITLE, tileval);
        data.putExtra(Constants.EXTRA_DESCRIPTION, desc);
        data.putExtra(Constants.EXTRA_PRIORITY, priorityval);
        int id = getIntent().getIntExtra(Constants.EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(Constants.EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();

    }
}
