package com.example.expensetracker.repository;

import com.example.expensetracker.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    Optional<Budget> findFirstByUserEmailAndEndDateAfter(
            String userEmail,
            LocalDate today
    );
}
