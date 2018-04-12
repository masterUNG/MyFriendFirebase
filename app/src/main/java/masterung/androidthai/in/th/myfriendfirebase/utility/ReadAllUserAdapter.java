package masterung.androidthai.in.th.myfriendfirebase.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import masterung.androidthai.in.th.myfriendfirebase.R;

public class ReadAllUserAdapter extends RecyclerView.Adapter<ReadAllUserAdapter.MyViewHolder>{

    private Context context;
    private List<String> displayNameStringList,
            emailStringList, iconStringList;
    private LayoutInflater layoutInflater;

    public ReadAllUserAdapter(Context context,
                              List<String> displayNameStringList,
                              List<String> emailStringList,
                              List<String> iconStringList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.displayNameStringList = displayNameStringList;
        this.emailStringList = emailStringList;
        this.iconStringList = iconStringList;
    }   // Constructor

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recycleview_read_all, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String displayNameString = displayNameStringList.get(position);
        String emailString = emailStringList.get(position);
        String urlIconString = iconStringList.get(position);

//        Show TextView
        holder.displayNameTextView.setText(displayNameString);
        holder.emailTextView.setText(emailString);

//        Show Icon
        Picasso.get()
                .load(urlIconString)
                .into(holder.circleImageView);


    }

    @Override
    public int getItemCount() {
        return displayNameStringList.size();
    }

    //    Create Inner Class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView displayNameTextView, emailTextView;
        private CircleImageView circleImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

//            Intial View
            displayNameTextView = itemView.findViewById(R.id.txtDisplayName);
            emailTextView = itemView.findViewById(R.id.txtEmail);
            circleImageView = itemView.findViewById(R.id.circleImageView);


        }
    }   // MyViewHolder


}   // Main Class
