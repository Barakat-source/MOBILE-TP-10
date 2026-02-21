package ma.projet.tp10_firebase;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MsgViewHolder extends RecyclerView.ViewHolder {
    public TextView msgTv, authorTv;

    public MsgViewHolder(View itemView) {
        super(itemView);
        msgTv = itemView.findViewById(R.id.textView);
        authorTv = itemView.findViewById(R.id.textView2);
    }
}
