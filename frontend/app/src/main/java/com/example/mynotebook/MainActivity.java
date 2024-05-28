package com.example.mynotebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NoteServerCommunication {

    TextView title;
    Button createNoteButton;

    GridView gridView;

    int[] colorArray = {
            R.color.yellow,
            R.color.blue,
            R.color.pink,
            R.color.purple,
            R.color.orange,
            R.color.lime,
            R.color.green,
            R.color.pink2,
            R.color.cyan
    };

    public class CustomArrayAdapter extends ArrayAdapter<DataBase.Notes> {
        private Context context;
        private int resource;


        public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<DataBase.Notes> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DataBase.Notes notes = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,
                    parent, false);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(colorArray.length);

        int randomColor = ContextCompat.getColor(context, colorArray[randomIndex]);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(randomColor);
        drawable.setCornerRadius(26f);

        convertView.setBackground(drawable);

        TextView viewID =convertView.findViewById(R.id.listViewID);
        viewID.setText(String.valueOf(notes.getID()));
        TextView viewTitle =convertView.findViewById(R.id.listViewTitle);
        viewTitle.setText(notes.getTitle());
        TextView viewSubtitle =convertView.findViewById(R.id.listViewSubtitle);
        viewSubtitle.setText(notes.getSubtitle());
        TextView viewDescription =convertView.findViewById(R.id.listViewDesc);
        viewDescription.setText(notes.getDescription());
        TextView viewDate =convertView.findViewById(R.id.listViewDate);
        viewDate.setText(notes.getDate());
        return  convertView;

    }
}

    protected void FillListView(){
        try(DataBase db = new DataBase(getApplicationContext())){
            List<DataBase.Notes> notes = db.select();
            CustomArrayAdapter arrayAdapter = new CustomArrayAdapter(
                    this,
                    R.layout.activity_list_notes,
                    notes
            );
            gridView.clearChoices();
            gridView.setAdapter(arrayAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        FillListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.title = findViewById(R.id.notebookTextView);
        this.createNoteButton = findViewById(R.id.createButton);
        this.gridView = findViewById(R.id.gridView);
        FillListView();
        APICallGetAllNotes();

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            String Title = ((TextView)(view.findViewById(R.id.listViewTitle))).getText().toString();
            String Subtitle = ((TextView)(view.findViewById(R.id.listViewSubtitle))).getText().toString();
            String Decsription = ((TextView)(view.findViewById(R.id.listViewDesc))).getText().toString();
            String Date = ((TextView)(view.findViewById(R.id.listViewDate))).getText().toString();
            String ID = ((TextView)(view.findViewById(R.id.listViewID))).getText().toString();

            Bundle b = new Bundle();
            b.putString("Title", Title);
            b.putString("Subtitle", Subtitle);
            b.putString("Description", Decsription);
            b.putString("Date", Date);
            b.putString("ID", ID);

            Intent intent = new Intent(MainActivity.this,UpdateDelete.class);
            intent.putExtras(b);
            APICallSelectNote(Integer.parseInt(ID));

            startActivityForResult(intent, 200, b);

        });

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNote.class);
                startActivity(intent);
            }
        });
    }
}

