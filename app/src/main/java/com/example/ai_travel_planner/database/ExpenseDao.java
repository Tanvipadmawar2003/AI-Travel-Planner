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
    @androidx.room.Update
    void updateExpense(Expense expense);

    @Query("SELECT * FROM expense_table")
    List<Expense> getAllExpenses();

    @Query("SELECT SUM(amount) FROM expense_table")
    double getTotalExpense();

    @Query("SELECT COUNT(*) FROM expense_table")
    int getExpenseCount();

    // Category-wise Expense Summary

    @Query("SELECT SUM(amount) FROM expense_table WHERE category='Food'")
    double getFoodExpense();

    @Query("SELECT SUM(amount) FROM expense_table WHERE category='Hotel'")
    double getHotelExpense();

    @Query("SELECT SUM(amount) FROM expense_table WHERE category='Transport'")
    double getTransportExpense();

    @Query("SELECT SUM(amount) FROM expense_table WHERE category='Shopping'")
    double getShoppingExpense();

    @Query("SELECT SUM(amount) FROM expense_table WHERE category='Ticket'")
    double getTicketExpense();

    @Query("SELECT SUM(amount) FROM expense_table WHERE category='Other'")
    double getOtherExpense();

    @Query("SELECT * FROM expense_table WHERE expenseName LIKE '%' || :keyword || '%'")
    List<Expense> searchExpense(String keyword);

    @Query("SELECT * FROM expense_table WHERE category = :category")
    List<Expense> getExpenseByCategory(String category);

}