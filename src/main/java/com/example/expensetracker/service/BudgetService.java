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

    // CREATE BUDGET
    public Budget createBudget(
            String email,
            double amount,
            String period
    ) {
        LocalDate start = LocalDate.now();
        LocalDate end;

        switch (period.toUpperCase()) {
            case "WEEK" -> end = start.plusWeeks(1);
            case "MONTH" -> end = start.plusMonths(1);
            default -> end = start.plusYears(1);
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

    // GET ACTIVE BUDGET
    public Budget getActiveBudget(String email) {
        return budgetRepository
                .findFirstByUserEmailAndEndDateAfter(
                        email,
                        LocalDate.now()
                )
                .orElse(null);
    }
}
