package ma.projet.tp10_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private MsgAdapter msgAdapter;
    private ArrayList<Message> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);

        msgList = new ArrayList<>();
        msgAdapter = new MsgAdapter(msgList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(msgAdapter);

        floatingActionButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddMessage.class);
            startActivity(i);
        });

        loadMessages();
    }

    private void loadMessages() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("msg");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message value = data.getValue(Message.class);
                    msgList.add(value);
                }
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
