package com.example.ai_travel_planner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.R;
import com.example.ai_travel_planner.database.Expense;
import com.example.ai_travel_planner.database.TripDatabase;

import java.util.List;

public class ExpenseAdapter
        extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    List<Expense> expenseList;

    // Callback Interface
    public interface OnExpenseDeleted {
        void onDeleted();
    }

    private OnExpenseDeleted listener;

    public ExpenseAdapter(
            List<Expense> expenseList,
            OnExpenseDeleted listener) {

        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_expense,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Expense expense = expenseList.get(position);

        holder.tvExpenseName.setText(expense.expenseName);

        holder.tvCategory.setText(
                "Category : " + expense.category
        );

        holder.tvAmount.setText(
                "₹ " + expense.amount
        );

        holder.btnDelete.setOnClickListener(v -> {

            int currentPosition = holder.getAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            TripDatabase.getInstance(
                            holder.itemView.getContext())
                    .expenseDao()
                    .deleteExpense(expense);

            expenseList.remove(currentPosition);

            notifyItemRemoved(currentPosition);

            notifyItemRangeChanged(
                    currentPosition,
                    expenseList.size()
            );

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Expense Deleted",
                    Toast.LENGTH_SHORT
            ).show();

            if (listener != null) {
                listener.onDeleted();
            }

        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvExpenseName;
        TextView tvCategory;
        TextView tvAmount;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvExpenseName =
                    itemView.findViewById(R.id.tvExpenseName);

            tvCategory =
                    itemView.findViewById(R.id.tvCategory);

            tvAmount =
                    itemView.findViewById(R.id.tvAmount);

            btnDelete =
                    itemView.findViewById(R.id.btnDelete);
        }
    }
}