package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // ➕ Create expense
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody Expense expense, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        expense.setUserId(userId);
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        return ResponseEntity.ok(expenseRepository.save(expense));
    }

    // 📄 Get all expenses by user
    @GetMapping
    public ResponseEntity<?> getUserExpenses(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return ResponseEntity.ok(expenses);
    }

    // ✏️ Update expense
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable String id, @RequestBody Expense updatedExpense, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        return expenseRepository.findById(id)
                .map(expense -> {
                    if (!expense.getUserId().equals(userId))
                        return ResponseEntity.status(403).body("Forbidden");

                    expense.setTitle(updatedExpense.getTitle());
                    expense.setAmount(updatedExpense.getAmount());
                    expense.setDate(updatedExpense.getDate());
                    expense.setCategory(updatedExpense.getCategory());
                    return ResponseEntity.ok(expenseRepository.save(expense));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ❌ Delete expense
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable String id, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        return expenseRepository.findById(id)
                .map(expense -> {
                    if (!expense.getUserId().equals(userId))
                        return ResponseEntity.status(403).body("Forbidden");
                    expenseRepository.delete(expense);
                    return ResponseEntity.ok("Deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
