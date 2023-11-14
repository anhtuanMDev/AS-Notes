package com.example.notes.Adater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.DAO.Dao;
import com.example.notes.Model.NotesModel;
import com.example.notes.R;
import com.example.notes.TakeNotes;

import java.util.ArrayList;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecycleView_ViewHolder> {

    Context context;
    ArrayList<NotesModel> notes;
    Dao dao;

    public RecyclerView_Adapter(Context context, ArrayList<NotesModel> notes, Dao dao) {
        this.context = context;
        this.notes = notes;
        this.dao = dao;
    }

    @NonNull
    @Override
    public RecycleView_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_viewholder, parent, false);
        return new RecycleView_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_ViewHolder holder, int position) {
        NotesModel model = notes.get(position);
        holder.title.setText(model.getTitle());
        holder.title.setTextColor(Color.parseColor(model.getTextColor()));
        holder.background.setBackgroundColor(Color.parseColor(model.getBackgroundColor()));

        holder.bind(model, view -> {
            Intent intent = new Intent(context, TakeNotes.class);
            intent.putExtra("title", model.getTitle());
            intent.putExtra("content", model.getContent());
            intent.putExtra("colorText", model.getTextColor());
            intent.putExtra("colorBackground", model.getBackgroundColor());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

class RecycleView_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    LinearLayout background;
    TextView title;

    public RecycleView_ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.background = itemView.findViewById(R.id.holder_background);
        this.title = itemView.findViewById(R.id.holderTitle);
        itemView.setOnClickListener(this);
    }

    public void bind(NotesModel model, View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }


    @Override
    public void onClick(View v) {
    }
}
