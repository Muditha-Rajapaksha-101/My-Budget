package com.example.mybudget.rvAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudget.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class IncomeRVAdapter extends RecyclerView.Adapter<IncomeRVAdapter.IncomeRVViewHolder>{


    ArrayList<String> incomes = new ArrayList<>();
    OnRowClickListener onRowClickListener;

    public IncomeRVAdapter(ArrayList<String> incomes , OnRowClickListener onRowClickListener) {
        this.incomes = incomes;
        this.onRowClickListener = onRowClickListener;
    }

    @NonNull
    @Override
    public IncomeRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transactionView = inflater.inflate(R.layout.rvincomerow,parent,false);
        IncomeRVAdapter.IncomeRVViewHolder viewHolder = new IncomeRVAdapter.IncomeRVViewHolder(transactionView,onRowClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeRVViewHolder holder, int position) {
        String currentIncome =  incomes.get(position);
        holder.txtRVRowIncome.setText(currentIncome);

    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public class IncomeRVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtRVRowIncome;
        OnRowClickListener onRowClickListener;

        public IncomeRVViewHolder(@NonNull View itemView , OnRowClickListener onRowClickListener) {
            super(itemView);
            txtRVRowIncome = itemView.findViewById(R.id.txtRvRowIncome);
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
