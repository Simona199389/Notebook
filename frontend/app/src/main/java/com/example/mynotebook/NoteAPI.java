package com.example.mynotebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NoteAPI {
    public class message{
        public String status;
        public message(message m){
            this.status = m.status;
        }
        public message(String _status){
            this.status = _status;
        }
    }
    public class  notes{
        public String Title;
        public String Subtitle;
        public String Description;
        public int ID;
        public String Date;
        public notes(notes n){
            Title = n.Title;
            Subtitle = n.Subtitle;
            Description = n.Description;
            ID = n.ID;
            Date = n.Date;
        }
        public notes(String _Title, String _Subtitle,
                     String _Description,int _ID, String _Date) {
            Title = _Title;
            Subtitle = _Subtitle;
            Description = _Description;
            ID = _ID;
            Date = _Date;
        }

    }
    @GET("/api/notes")//done
    public Call<List<notes>> api_notes();

    @GET("/api/notes/{id}")//done
    public Call<notes> api_get_note(@Path("id") int id);

    @POST("/api/notes/add")//done
    public Call<notes> api_add_note(@Body notes c);

    @PUT("/api/notes/update")//done
    public Call<notes> api_update_note(@Body notes c);

    @DELETE("/api/notes/delete/{id}")//done
    public Call<message> api_delete_note(@Path("id") int id);


}
