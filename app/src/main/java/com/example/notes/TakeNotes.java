package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.notes.DAO.Dao;
import com.example.notes.Model.NotesModel;
import com.example.notes.SQLite.SQlite;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class TakeNotes extends AppCompatActivity {

    SQlite sQlite = new SQlite(this);
    ;
    Dao dao = new Dao(this, sQlite);
    EditText title, content;
    ImageView back, see, save, change;
    ToggleButton toggle;
    Button tColor, bColor;
    LinearLayout layoutColor;
    boolean seeMode = true;

    Intent intent;
    boolean customColor;
    String header, subLine, textColor, backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_notes);

        title = findViewById(R.id.input_title);
        content = findViewById(R.id.input_content);
        save = findViewById(R.id.takeNote_save);
        see = findViewById(R.id.takeNote_see);
        change = findViewById(R.id.takeNote_saveChange);
        back = findViewById(R.id.takeNote_back);
        toggle = findViewById(R.id.takeNote_toggle);
        layoutColor = findViewById(R.id.select_color);
        tColor = findViewById(R.id.textColor);
        bColor = findViewById(R.id.backgroundColor);

        intent = getIntent();

        if (intent != null) {
            header = intent.getStringExtra("title");
            subLine = intent.getStringExtra("content");
            textColor = intent.getStringExtra("colorText");
            backgroundColor = intent.getStringExtra("colorBackground");
        }

        customColor = header != null;

        // Sau khi gán giá trị mặc định cho textColor và backgroundColor
        if (textColor != null && backgroundColor != null) {
            changColor(Color.parseColor(textColor), Color.parseColor(backgroundColor));
        } else {
            backgroundColor = dao.generateRandomHexColor();
            textColor = dao.findTextColor(backgroundColor);
            changColor(Color.parseColor(textColor), Color.parseColor(backgroundColor));
        }

        if (customColor) {
            layoutColor.setVisibility(View.VISIBLE);
            toggle.setChecked(customColor);
            title.setText(header);
            content.setText(subLine);
        } else {
            layoutColor.setVisibility(View.GONE);
            toggle.setChecked(customColor);
        }

        tColor.setOnClickListener(v -> {
            int initialColor = Color.parseColor(textColor);
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    textColor = String.format("#%06X", (0xFFFFFF & color));
                    tColor.setBackgroundColor(Color.parseColor(textColor));
                }
            });
            dialog.show();
        });

        bColor.setOnClickListener(v -> {
            int initialColor = Color.parseColor(backgroundColor);
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    backgroundColor = String.format("#%06X", (0xFFFFFF & color));
                    bColor.setBackgroundColor(Color.parseColor(backgroundColor));
                }
            });
            dialog.show();
        });


        toggle.setOnClickListener(v -> {
            if (toggle.isChecked()) {
                layoutColor.setVisibility(View.VISIBLE);
            } else {
                layoutColor.setVisibility(View.GONE);
            }
        });


        if (header != null) {
            title.setText(header);
            title.setTag(header);
            change.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
        }
        if (subLine != null) {
            content.setText(subLine);
            content.setTag(subLine);
        }

        title.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                content.requestFocus();
                return true; // Consume the event
            }
            return false; // Don't consume the event
        });

        save.setOnClickListener(v -> {
            String header = title.getText().toString();
            String subLine = content.getText().toString();
            if (header.length() != 0) {
                dao.insertNote(header, subLine, textColor, backgroundColor);
                Toast.makeText(TakeNotes.this, "Save", Toast.LENGTH_SHORT).show();
                changActivity();
            } else {
                Toast.makeText(TakeNotes.this, "We at least need title to save the note", Toast.LENGTH_SHORT).show();
            }
        });

        change.setOnClickListener(v -> {
            String header1 = title.getText().toString();
            String subLine1 = content.getText().toString();
            ArrayList<NotesModel> notes = dao.getColors(header);
            String tcolor = notes.get(0).getTextColor();
            String backcolor = notes.get(0).getBackgroundColor();
            if (!header1.equals(header) || !subLine.equals(subLine1) ||
                    !tcolor.equals(textColor) || !backcolor.equals(backgroundColor)) {
                dao.editNotes(header, header1, subLine1, textColor, backgroundColor);
                Toast.makeText(TakeNotes.this, "Note has been changed", Toast.LENGTH_SHORT).show();
                header = header1;
                subLine = subLine1;
            } else {
                Toast.makeText(TakeNotes.this, "There nothing to save", Toast.LENGTH_SHORT).show();
            }
        });

        see.setOnClickListener(v -> {
            String header12 = title.getText().toString();
            if (header12.length() != 0 && seeMode) {
                title.setEnabled(false);
                content.setEnabled(false);
                see.setImageDrawable(getResources().getDrawable(R.drawable.baseline_edit_note_24));
                seeMode = false;
            } else {
                title.setEnabled(true);
                content.setEnabled(true);
                content.requestFocus();
                see.setImageDrawable(getResources().getDrawable(R.drawable.baseline_remove_red_eye_24));
                seeMode = true;
            }
        });

        back.setOnClickListener(v -> {
            String header1 = title.getText().toString();
            String subLine1 = content.getText().toString();
            if (!header1.equals(header) || !subLine.equals(subLine1)) {
                showDiscardDialog();
            } else {
                changActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String header1 = title.getText().toString();
        String subLine1 = content.getText().toString();
        if (!header1.equals(header) || !subLine.equals(subLine1)) {
            showDiscardDialog();
        } else {
            changActivity();
        }
    }

    public void changColor(int textColor, int backgroundColor) {
        tColor.setBackgroundColor(textColor);
        bColor.setBackgroundColor(backgroundColor);
    }

    public void showDiscardDialog() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.discard_dialog, null, false);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        Button keep = view.findViewById(R.id.keep);
        Button discard = view.findViewById(R.id.discard);
        discard.setOnClickListener(v -> {
            title.setTag(null);
            content.setTag(null);

            changActivity();
            dialog.dismiss();
        });
        keep.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void changActivity() {
        Intent intents = new Intent(this, MainActivity.class);
        startActivity(intents);
        finish();
    }

    public boolean checkDiscard(String headers) {
        ArrayList<NotesModel> header = dao.getNotes();
        for (NotesModel model : header) {
            if (model.getTitle().equals(headers)) {
                return false;
            }
        }
        return true;
    }
}