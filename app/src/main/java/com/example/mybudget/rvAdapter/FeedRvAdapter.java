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

public class FeedRvAdapter extends RecyclerView.Adapter<FeedRvAdapter.FeedRvViewHolder>{

    private ArrayList<String> transactions;
    private ArrayList<String> amounts;
    private ArrayList<String> dates;
    private ArrayList<String> types;
    private ArrayList<String> categories;
    private OnRvFeedRowClick onRvFeedRowClick;
    String currency;


    public FeedRvAdapter(ArrayList<String> transactions , ArrayList<String> amounts , ArrayList<String> dates , ArrayList<String> types , ArrayList<String> categories , OnRvFeedRowClick onRvFeedRowClick, String currency) {
        this.transactions = transactions;
        this.amounts = amounts;
        this.dates= dates;
        this.types= types;
        this.categories = categories;
        this.onRvFeedRowClick = onRvFeedRowClick;
        this.currency = currency;
    }

    @NonNull
    @Override
    public FeedRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transactionView = inflater.inflate(R.layout.rv_feed_row,parent,false);
        FeedRvViewHolder viewHolder = new FeedRvViewHolder(transactionView,onRvFeedRowClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRvViewHolder holder, int position) {
        String currentDescription =  transactions.get(position);

        //Covert the Float to Integer and to String
        String currentAmount1  = amounts.get(position);
        //String currentAmount  =  String.valueOf(Float.valueOf(currentAmount1).intValue());
        double amount = Double.parseDouble(currentAmount1);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String currentAmount = formatter.format(amount);

        String currentDate  = dates.get(position);
        String currentCategory  = categories.get(position);
        String currentType  = types.get(position);

        String currentCurrency = this.currency;

        //holder.txtRvRowDescription.setText(currentDescription);
        holder.txtRvRowAmount.setText(currentAmount);
        holder.txtRvRowDate.setText(currentDate);
        //holder.txtRvRowCategory.setText(currentCategory);
        holder.txtRvRowDescription.setText(currentCategory);
        holder.txtRvRowCategory.setText(currentDescription);
        holder.txtRvCurrency.setText(currentCurrency);


        if(currentType.equals("Income")){
            holder.txtRvRowDescription.setTextColor(Color.rgb(78, 128, 91));
            holder.txtRvRowAmount.setTextColor(Color.rgb(78, 128, 91));

        }else{
            holder.txtRvRowDescription.setTextColor(Color.rgb(158, 105, 96));
            holder.txtRvRowAmount.setTextColor(Color.rgb(158, 105, 96));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class FeedRvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView txtRvRowDescription;
         TextView txtRvRowAmount;
         TextView txtRvRowDate;
         TextView txtRvRowCategory;
         OnRvFeedRowClick onRvFeedRowClick;
         TextView txtRvCurrency;

         FeedRvViewHolder(@NonNull View itemView , OnRvFeedRowClick onRvFeedRowClick) {
            super(itemView);
            txtRvRowDescription = itemView.findViewById(R.id.txtRvRowDescription);
            txtRvRowAmount =itemView.findViewById(R.id.txtRvRowAmount);
            txtRvRowDate =itemView.findViewById(R.id.txtRvRowDate);
            txtRvRowCategory =itemView.findViewById(R.id.txtRvRowCategory);
            txtRvCurrency = itemView.findViewById(R.id.txtRvCurrency);
            this.onRvFeedRowClick = onRvFeedRowClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRvFeedRowClick.onRvFeedRowClickEvent(view,getAdapterPosition());
        }
    }

    public interface OnRvFeedRowClick {
        public void onRvFeedRowClickEvent(View view , int position);
    }
}
