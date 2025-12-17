package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // ✅ CREATE EXPENSE
    @PostMapping
    public ResponseEntity<Expense> addExpense(
            Authentication authentication,
            @RequestBody Expense expense
    ) {
        expense.setUserEmail(authentication.getName());
        Expense saved = expenseService.addExpense(expense);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET ALL USER EXPENSES
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            Authentication authentication
    ) {
        List<Expense> expenses =
                expenseService.getExpenses(authentication.getName());
        return ResponseEntity.ok(expenses);
    }

    // ✅ UPDATE EXPENSE
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable String id,
            Authentication authentication,
            @RequestBody Expense expense
    ) {
        Expense updated = expenseService.updateExpense(
                id,
                authentication.getName(),
                expense
        );
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE EXPENSE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable String id,
            Authentication authentication
    ) {
        expenseService.deleteExpense(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
