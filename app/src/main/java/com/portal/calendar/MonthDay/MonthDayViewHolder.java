package com.portal.calendar.MonthDay;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.portal.calendar.R;

public class MonthDayViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public final TextView dayOfMonth_txt;
    private final MonthDayAdapter.OnItemListener onItemListener;

    private int day;
    public MonthDayViewHolder(@NonNull View itemView, MonthDayAdapter.OnItemListener onItemListener) {
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
