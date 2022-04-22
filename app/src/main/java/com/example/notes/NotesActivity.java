package com.example.notes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NotesActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    ImageView mcreatenote;
    private FirebaseAuth firebaseAuth;

    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);

        mcreatenote = findViewById(R.id.createNote);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        Objects.requireNonNull(getSupportActionBar()).setTitle("All Notes");

        mcreatenote.setOnClickListener(view -> startActivity(new Intent(NotesActivity.this, CreateNote.class)));

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        // get all note to recycler
        FirestoreRecyclerOptions<FirebaseModel> allusernotes = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query, FirebaseModel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allusernotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel firebasemodel) {
//                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.menupopbutton);

                int colourcode = getRandomColor();
//                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));

                noteViewHolder.setNote(firebasemodel);

                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(view -> {
                    // mở note detail activity

                    Intent intent = new Intent(view.getContext(), NoteDetails.class);
                    intent.putExtra("title", firebasemodel.getTitle());
                    intent.putExtra("content", firebasemodel.getContent());
                    intent.putExtra("noteId", docId);

                    view.getContext().startActivity(intent);

                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mRecyclerView = findViewById(R.id.notesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(noteAdapter);

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textSubtitle;
        private TextView textDateTime;
        LinearLayout layoutNote;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);

        }

        void setNote(FirebaseModel firebaseModel) {
            textTitle.setText(firebaseModel.getTitle());
            if (firebaseModel.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(firebaseModel.getSubtitle());
            }
            textDateTime.setText(firebaseModel.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (firebaseModel.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(firebaseModel.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // switch để thêm menu item
        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(NotesActivity.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    private int getRandomColor() {
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.green);
        colorcode.add(R.color.lightgreen);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);


        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);
    }

}