package com.example.expensetracker.service;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // ================= CREATE BUDGET =================
    public Budget createBudget(
            String email,
            double amount,
            String period
    ) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Budget amount must be positive");
        }

        LocalDate start = LocalDate.now();
        LocalDate end;

        switch (period.toUpperCase()) {
            case "WEEK" -> end = start.plusWeeks(1);
            case "MONTH" -> end = start.plusMonths(1);
            case "YEAR" -> end = start.plusYears(1);
            default -> throw new IllegalArgumentException(
                    "Invalid period. Use WEEK, MONTH, or YEAR"
            );
        }

        Budget budget = new Budget(
                null,
                email,
                amount,
                period.toUpperCase(),
                start,
                end
        );

        return budgetRepository.save(budget);
    }

    // ================= GET ACTIVE BUDGET =================
    public Budget getActiveBudget(String email) {
        return budgetRepository
                .findFirstByUserEmailAndEndDateAfter(
                        email,
                        LocalDate.now()
                )
                .orElse(null);
    }

    // ================= REMAINING BUDGET =================
    // (Simple version for class project)
    public double getRemainingBudget(String email) {

        Budget budget = budgetRepository
                .findFirstByUserEmailAndEndDateAfter(
                        email,
                        LocalDate.now()
                )
                .orElseThrow(() ->
                        new IllegalStateException("No active budget")
                );

        // No expense subtraction yet (safe & acceptable for class project)
        return budget.getAmount();
    }
}
