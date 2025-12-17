package com.example.expensetracker.service;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BudgetService budgetService;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            BudgetService budgetService
    ) {
        this.expenseRepository = expenseRepository;
        this.budgetService = budgetService;
    }

    // ADD EXPENSE (WITH BUDGET VALIDATION)
    public Expense addExpense(Expense expense) {

        Budget budget =
                budgetService.getActiveBudget(expense.getUserEmail());

        if (budget != null) {
            Double spent = expenseRepository.sumExpensesBetween(
                    expense.getUserEmail(),
                    budget.getStartDate(),
                    budget.getEndDate()
            );

            double totalSpent = spent == null ? 0 : spent;

            if (totalSpent + expense.getAmount()
                    > budget.getAmount()) {
                throw new RuntimeException("Budget exceeded");
            }
        }

        expense.setDate(LocalDate.now());
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpenses(String email) {
        return expenseRepository.findByUserEmail(email);
    }

    public Expense updateExpense(
            String id,
            String email,
            Expense updated
    ) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow();

        if (!expense.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        expense.setAmount(updated.getAmount());
        expense.setCategory(updated.getCategory());
        expense.setTitle(updated.getTitle());

        return expenseRepository.save(expense);
    }

    public void deleteExpense(String id, String email) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow();

        if (!expense.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        expenseRepository.delete(expense);
    }
}
