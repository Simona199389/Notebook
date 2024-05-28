package com.example.mynotebook;

import android.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface NoteServerCommunication {

    public interface ShowMessage {
        public void Message(String message);
    }

    public default void APICallInsert(NoteAPI.notes note, ShowMessage message) {

        Thread t = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:5000/api/notes/add/").addConverterFactory(GsonConverterFactory.create()).client(client).build();
                NoteAPI api = retrofit.create(NoteAPI.class);
                Call<NoteAPI.notes> insertedUser = api.api_add_note(note);
                Response<NoteAPI.notes> r = insertedUser.execute();
                if (r.isSuccessful()) {
                    NoteAPI.notes resp = r.body();
                    message.Message("Inserted in server with id " + resp.ID);
                }

            } catch (Exception e) {

                message.Message("Error from server " + e.getLocalizedMessage());
                Log.e("NoteAPI", "Error from server " + e.getLocalizedMessage());
            }

        });
        t.start();
    }

    public default void APICallGetAllNotes() {
        Thread t = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("t").addConverterFactory(GsonConverterFactory.create()).client(client).build();
                NoteAPI api = retrofit.create(NoteAPI.class);
                Call<List<NoteAPI.notes>> fetchNotes = api.api_notes();
                Log.e("NoteAPI", "Success from server " + fetchNotes);
            } catch (Exception e) {
                Log.e("NoteAPI", "Error from server " + e.getLocalizedMessage());
            }
            ;

        });
        t.start();
    }
    public default void APICallSelectNote(int id) {
        Thread t = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:5000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
                NoteAPI api = retrofit.create(NoteAPI.class);
                Call<NoteAPI.notes> fetchNoteCall = api.api_get_note(id);

                fetchNoteCall.enqueue(new Callback<NoteAPI.notes>() {
                    @Override
                    public void onResponse(Call<NoteAPI.notes> call, Response<NoteAPI.notes> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            NoteAPI.notes note = response.body();
                            Log.e("NoteAPI", "Success from server: " + note.toString());
                        } else {
                            Log.e("NoteAPI", "Server returned error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<NoteAPI.notes> call, Throwable t) {
                        Log.e("NoteAPI", "Error fetching note: " + t.getLocalizedMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("NoteAPI", "Error initializing select request: " + e.getLocalizedMessage());
            }
        });
        t.start();
    }

    public default void APICallDeleteNote(int id) {
        Thread t = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:5000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
                NoteAPI api = retrofit.create(NoteAPI.class);
                Call<NoteAPI.message> call = api.api_delete_note(id);
                call.enqueue(new Callback<NoteAPI.message>() {
                    @Override
                    public void onResponse(Call<NoteAPI.message> call, Response<NoteAPI.message> response) {
                        if (response.isSuccessful()) {
                            Log.e("NoteAPI", "Success: " + response.body());
                        } else {
                            Log.e("NoteAPI", "Server returned an error: " + response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<NoteAPI.message> call, Throwable t) {
                        Log.e("NoteAPI", "Error from server: " + t.getLocalizedMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("NoteAPI", "Error initializing delete request: " + e.getLocalizedMessage());
            }
        });
        t.start();
    }

    public default void APICallUpdateNote(NoteAPI.notes note) {
        Thread t = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:5000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
                NoteAPI api = retrofit.create(NoteAPI.class);
                Call<NoteAPI.notes> call = api.api_update_note(note);
                call.enqueue(new Callback<NoteAPI.notes>() {
                    @Override
                    public void onResponse(Call<NoteAPI.notes> call, Response<NoteAPI.notes> response) {
                        if (response.isSuccessful()) {
                            Log.e("NoteAPI", "Success: " + response.body());
                        } else {
                            Log.e("NoteAPI", "Server returned an error: " + response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<NoteAPI.notes> call, Throwable t) {
                        Log.e("NoteAPI", "Error from server: " + t.getLocalizedMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("NoteAPI", "Error initializing update request: " + e.getLocalizedMessage());
            }
        });
        t.start();
    }
}
