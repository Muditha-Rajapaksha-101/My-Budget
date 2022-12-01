package com.example.mybudget.rvAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudget.R;

import java.util.ArrayList;

public class ExpenseRVAdapter extends RecyclerView.Adapter<ExpenseRVAdapter.ExpenseRVViewHolder>{

    ArrayList<String> expenses = new ArrayList<>();
    ExpenseRVAdapter.OnRowClickListener onRowClickListener;

    public ExpenseRVAdapter(ArrayList<String> expenses , ExpenseRVAdapter.OnRowClickListener onRowClickListener) {
        this.expenses = expenses;
        this.onRowClickListener = onRowClickListener;
    }

    @NonNull
    @Override
    public ExpenseRVAdapter.ExpenseRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View expenseVew = inflater.inflate(R.layout.rvexpenserow,parent,false);
        ExpenseRVAdapter.ExpenseRVViewHolder viewHolder = new ExpenseRVAdapter.ExpenseRVViewHolder(expenseVew,onRowClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseRVAdapter.ExpenseRVViewHolder holder, int position) {
        String currentExpense =  expenses.get(position);
        holder.txtRVRowExpense.setText(currentExpense);

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ExpenseRVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtRVRowExpense;
        ExpenseRVAdapter.OnRowClickListener onRowClickListener;

        public ExpenseRVViewHolder(@NonNull View itemView , ExpenseRVAdapter.OnRowClickListener onRowClickListener) {
            super(itemView);
            txtRVRowExpense = itemView.findViewById(R.id.txtRvRowExpense);
            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRowClickListener.onRVRowClick(view,getAdapterPosition());
        }
    }

    public interface OnRowClickListener{
        public void onRVRowClick(View view , int position);
    }

}
