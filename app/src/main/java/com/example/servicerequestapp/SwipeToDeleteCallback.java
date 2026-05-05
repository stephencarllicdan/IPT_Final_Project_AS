package com.example.servicerequestapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final Drawable deleteIcon;
    private final ColorDrawable background;

    public SwipeToDeleteCallback(Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // ✅ FIXED

        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete);

        if (deleteIcon == null) {
            throw new RuntimeException("ic_delete not found");
        }

        background = new ColorDrawable(Color.parseColor("#E53935"));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState,
                            boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;

        if (dX > 0) {
            background.setBounds(
                    itemView.getLeft(),
                    itemView.getTop(),
                    itemView.getLeft() + (int) dX,
                    itemView.getBottom()
            );

            deleteIcon.setBounds(
                    itemView.getLeft() + iconMargin,
                    itemView.getTop() + iconMargin,
                    itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth(),
                    itemView.getBottom() - iconMargin
            );

        } else if (dX < 0) {
            background.setBounds(
                    itemView.getRight() + (int) dX,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
            );

            deleteIcon.setBounds(
                    itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth(),
                    itemView.getTop() + iconMargin,
                    itemView.getRight() - iconMargin,
                    itemView.getBottom() - iconMargin
            );
        } else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        deleteIcon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}