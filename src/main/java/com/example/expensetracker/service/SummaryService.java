package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class SummaryService {

    private final ExpenseRepository expenseRepository;

    public SummaryService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public double dailyTotal(String email, LocalDate date) {
        return expenseRepository.findByUserEmail(email)
                .stream()
                .filter(e -> e.getDate().equals(date))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double monthlyTotal(String email, YearMonth month) {
        return expenseRepository.findByUserEmail(email)
                .stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
