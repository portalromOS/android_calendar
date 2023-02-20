package com.portal.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public final TextView dayOfMonth_txt;
    private final CalendarAdapter.OnItemListener onItemListener;

    private int day;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth_txt = itemView.findViewById(R.id.cellEventTextView);

        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    public void setDayOfMonth(int day){
        this.day = day;
        if(day>0)
            dayOfMonth_txt.setText(day+"");
        else
            dayOfMonth_txt.setText("");
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), day);
    }
}
