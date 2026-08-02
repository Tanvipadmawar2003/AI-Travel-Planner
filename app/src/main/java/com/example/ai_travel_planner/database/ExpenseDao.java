package com.example.ai_travel_planner.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insertExpense(Expense expense);
    @Delete
    void deleteExpense(Expense expense);

    @Query("SELECT * FROM expense_table")
    List<Expense> getAllExpenses();

    @Query("SELECT SUM(amount) FROM expense_table")
    double getTotalExpense();

    @Query("SELECT COUNT(*) FROM expense_table")
    int getExpenseCount();

}