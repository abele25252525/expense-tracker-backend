package com.example.expensetracker.controller;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // ================= CREATE BUDGET =================
    @PostMapping
    public ResponseEntity<?> createBudget(
            Authentication authentication,
            @RequestParam double amount,
            @RequestParam String period
    ) {
        try {
            Budget budget = budgetService.createBudget(
                    authentication.getName(),
                    amount,
                    period
            );
            return ResponseEntity.ok(budget);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    // ================= GET ACTIVE BUDGET =================
    @GetMapping
    public ResponseEntity<?> getBudget(Authentication authentication) {
        Budget budget = budgetService.getActiveBudget(authentication.getName());

        if (budget == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No active budget");
        }

        return ResponseEntity.ok(budget);
    }

    // ================= REMAINING BUDGET =================
    @GetMapping("/remaining")
    public ResponseEntity<Double> remaining(Authentication authentication) {
        return ResponseEntity.ok(
                budgetService.getRemainingBudget(authentication.getName())
        );
    }
}
