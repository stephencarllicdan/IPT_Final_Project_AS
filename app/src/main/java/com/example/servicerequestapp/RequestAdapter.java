package com.example.servicerequestapp;

import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    ArrayList<RequestModel> list;

    public interface OnItemActionListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    private OnItemActionListener listener;

    public void setOnItemActionListener(OnItemActionListener listener){
        this.listener = listener;
    }

    public RequestAdapter(ArrayList<RequestModel> list){
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, status;
        ImageView icon;

        public ViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.titleText);
            status = v.findViewById(R.id.statusText);
            icon = v.findViewById(R.id.statusIcon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {

        RequestModel r = list.get(position);

        h.title.setText(r.title);

        String status = r.status == null ? "pending" : r.status.trim().toLowerCase();

        // 🔥 STATUS UI
        if (status.equals("done")) {
            h.status.setText("Completed");
            h.status.setTextColor(0xFF2E7D32);
            h.icon.setImageResource(R.drawable.ic_done);
        } else {
            h.status.setText("Pending");
            h.status.setTextColor(0xFFC62828);
            h.icon.setImageResource(R.drawable.ic_pending);
        }

        // 🔥 CLICK (open options)
        h.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.onClick(position);
            }
        });

        // 🔥 LONG CLICK (quick edit or done)
        h.itemView.setOnLongClickListener(v -> {
            if(listener != null){
                listener.onLongClick(position);
            }
            return true;
        });

        // 🔥 SMOOTH ANIMATION (better than fade_in)
        h.itemView.setAnimation(
                AnimationUtils.loadAnimation(
                        h.itemView.getContext(),
                        android.R.anim.slide_in_left
                )
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}