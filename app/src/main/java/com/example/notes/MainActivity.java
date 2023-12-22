package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.Adater.RecyclerView_Adapter;
import com.example.notes.DAO.Dao;
import com.example.notes.Model.NotesModel;
import com.example.notes.SQLite.SQlite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView logo;
    RecyclerView_Adapter adapter;
    TextView logoSubtile;
    FloatingActionButton addNotes;
    RecyclerView recyclerView;
    ArrayList<NotesModel> notes;
    Dao dao ;
    SQlite sQlite;
//    RecyclerView_Adapter adapter;
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    boolean trans = true;
    ItemTouchHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        logo = findViewById(R.id.empty_logo);
        logoSubtile = findViewById(R.id.empty_logo_subtitle);
        addNotes = findViewById(R.id.add_notes);
        recyclerView = findViewById(R.id.recyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");
        notes = new ArrayList<>();
        sQlite = new SQlite(this);
        dao = new Dao(this,sQlite);
        notes= dao.getNotes();

        hideShowLogo();
        addNotes.setOnClickListener(v -> changeActivity(new TakeNotes()));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter = new RecyclerView_Adapter(this,notes,dao);
        recyclerView.setAdapter(adapter);

        helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction){
                    case ItemTouchHelper.START:
                        dao.deleted(notes.get(viewHolder.getAdapterPosition()).getTitle());
                        notes.remove(viewHolder.getAdapterPosition());
                        Toast.makeText(MainActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        hideShowLogo();
                        break;
                }
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    public void changeActivity(AppCompatActivity activity){
        try {
            Intent intent = new Intent(MainActivity.this,activity.getClass());
            startActivity(intent);
        }catch (Exception e){
            Log.e("Activity", "error");
        }

    }

    public void hideShowLogo(){
        if(notes.size()==0){
            logo.setVisibility(View.VISIBLE);
            logoSubtile.setVisibility(View.VISIBLE);
        }else{
            logo.setVisibility(View.GONE);
            logoSubtile.setVisibility(View.GONE);
        }
    }
}