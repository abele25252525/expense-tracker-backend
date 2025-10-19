package com.example.expensetracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;
    private String title;
    private double amount;
    private LocalDate date;
    private String category;
    private String userId; // linked to JWT user

    public Expense() {}

    public Expense(String title, double amount, LocalDate date, String category, String userId) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.userId = userId;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
