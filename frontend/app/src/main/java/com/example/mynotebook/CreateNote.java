package com.example.mynotebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class CreateNote extends AppCompatActivity implements NoteServerCommunication {

    EditText editTitle, editSubtitle, editDescription;
    Date dateTime;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        editTitle = findViewById(R.id.editTextNoteTitle);
        editSubtitle = findViewById(R.id.editTextNoteSubTitle);
        editDescription = findViewById(R.id.editTextNoteDescription);
        createBtn = findViewById(R.id.createButton);
        dateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(dateTime);

        createBtn.setOnClickListener(view -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();

            if (!isValidTitle(title)) {
                Toast.makeText(getApplicationContext(), "Title must be more than 3 characters.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidDescription(description)) {
                Toast.makeText(getApplicationContext(), "Description must be less than 70 characters.", Toast.LENGTH_LONG).show();
                return;
            }

            try (DataBase db = new DataBase(getApplicationContext())) {
                long id = db.insert(
                        title,
                        editSubtitle.getText().toString(),
                        description,
                        formattedDateTime
                );

                Toast.makeText(getApplicationContext(), "Successfully!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CreateNote.this, MainActivity.class);
                startActivity(intent);

                finish();

                APICallInsert(
                        new NoteAPI.notes(
                                title,
                                editSubtitle.getText().toString(),
                                description,
                                Integer.valueOf((int) id),
                                formattedDateTime
                        ),
                        message -> runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show())
                );

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidTitle(String title) {
        return Pattern.matches(".{4,}", title);
    }

    private boolean isValidDescription(String description) {
        return Pattern.matches(".{0,69}", description);
    }
}
