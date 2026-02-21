package ma.projet.tp10_firebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgViewHolder> {

    private List<Message> messageList;

    public MsgAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msgitem, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        Message msg = messageList.get(position);
        holder.msgTv.setText(msg.getMsg());
        holder.authorTv.setText("- " + msg.getAuthor());

        // Delete on long click with confirmation
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("msg");
                        ref.child(msg.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Message deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), "Delete failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
            return true;
        });

        // Update on click
        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit Message");

            final EditText input = new EditText(v.getContext());
            input.setText(msg.getMsg());
            builder.setView(input);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("msg");
                    msg.setMsg(input.getText().toString());
                    ref.child(msg.getKey()).setValue(msg);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
