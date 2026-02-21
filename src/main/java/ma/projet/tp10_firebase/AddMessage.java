package ma.projet.tp10_firebase;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddMessage extends AppCompatActivity {

    private static final String TAG = "AddMessageActivity";
    private EditText text, text2;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        text = findViewById(R.id.editTextText);
        text2 = findViewById(R.id.editTextText2);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(v -> {
            String msg = text.getText().toString().trim();
            String author = text2.getText().toString().trim();

            if (msg.isEmpty()) {
                text.setError("Champ vide !");
                return;
            }

            if (author.isEmpty()) {
                text2.setError("Champ vide !");
                return;
            }

            addMsgDB(msg, author);
        });
    }

    private void addMsgDB(String msg, String author) {
        HashMap<String, Object> msgHashMap = new HashMap<>();
        msgHashMap.put("msg", msg);
        msgHashMap.put("author", author);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference msgRef = database.getReference("msg");

        String key = msgRef.push().getKey();
        msgHashMap.put("key", key);

        msgRef.child(key).setValue(msgHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "setValue:success");
                    Toast.makeText(AddMessage.this, "Message saved", Toast.LENGTH_SHORT).show();
                    finish(); // Return to MainActivity
                } else {
                    Log.e(TAG, "setValue:failure", task.getException());
                    Toast.makeText(AddMessage.this, "Error saving message. Check Logcat for details.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
