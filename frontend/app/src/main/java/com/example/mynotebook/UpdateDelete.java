package com.example.mynotebook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class UpdateDelete extends AppCompatActivity implements NoteServerCommunication {

    EditText editTitle, editSubtitle, editDesc;
    String originalTitle, originalSubtitle, originalDescription;
    String editDate;
    Button btnUpdate, btnDelete;
    Date dateTime;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        editTitle = findViewById(R.id.editTitle);
        editSubtitle = findViewById(R.id.editSubtitle);
        editDesc = findViewById(R.id.editDescription);
        btnUpdate = findViewById(R.id.editButton);
        btnDelete = findViewById(R.id.deleteButton);

        Bundle b = getIntent().getExtras();
        ID = Integer.valueOf(b.getString("ID"));

        originalTitle = b.getString("Title");
        originalSubtitle = b.getString("Subtitle");
        originalDescription = b.getString("Description");

        editTitle.setText(originalTitle);
        editSubtitle.setText(originalSubtitle);
        editDesc.setText(originalDescription);

        btnUpdate.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnUpdate.setEnabled(isAnyFieldEdited());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editTitle.addTextChangedListener(textWatcher);
        editSubtitle.addTextChangedListener(textWatcher);
        editDesc.addTextChangedListener(textWatcher);

        btnUpdate.setOnClickListener(view -> {
            String title = editTitle.getText().toString();
            String description = editDesc.getText().toString();

            if (!isValidTitle(title)) {
                Toast.makeText(getApplicationContext(), "Title must be more than 3 characters.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidDescription(description)) {
                Toast.makeText(getApplicationContext(), "Description must be less than 70 characters.", Toast.LENGTH_LONG).show();
                return;
            }
            dateTime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDateTime = dateFormat.format(dateTime);
            editDate = formattedDateTime;
            try (DataBase db = new DataBase(getApplicationContext())) {
                db.update(
                        title,
                        editSubtitle.getText().toString(),
                        description,
                        editDate,
                        ID
                );

                APICallUpdateNote(new NoteAPI.notes(
                        title,
                        editSubtitle.getText().toString(),
                        description,
                        ID,
                        formattedDateTime
                ));

                Toast.makeText(getApplicationContext(), "UPDATE OK", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            BackToMain();
        });

        btnDelete.setOnClickListener(view -> {
            try (DataBase db = new DataBase(getApplicationContext())) {
                db.delete(ID);
                Log.e("NoteAPI", "delete " + ID);

                Toast.makeText(getApplicationContext(), "Delete OK", Toast.LENGTH_LONG).show();

                APICallDeleteNote(ID);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            BackToMain();
        });
    }

    private void BackToMain() {
        finishActivity(201);
        Intent intent = new Intent(UpdateDelete.this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isValidTitle(String title) {
        return Pattern.matches(".{4,}", title);
    }

    private boolean isValidDescription(String description) {
        return Pattern.matches(".{0,69}", description);
    }

    private boolean isAnyFieldEdited() {
        String currentTitle = editTitle.getText().toString();
        String currentSubtitle = editSubtitle.getText().toString();
        String currentDescription = editDesc.getText().toString();

        return !currentTitle.equals(originalTitle) || !currentSubtitle.equals(originalSubtitle) || !currentDescription.equals(originalDescription);
    }
}
