package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

 List<Expense> findByUserEmail(String userEmail);

    @Aggregation(pipeline = {
        "{ $match: { userEmail: ?0, date: { $gte: ?1, $lte: ?2 } } }",
        "{ $group: { _id: null, total: { $sum: '$amount' } } }"
    })
    Double sumExpensesBetween(
            String email,
            LocalDate start,
            LocalDate end
    );
    Optional<Expense> findByIdAndUserEmail(String id, String userEmail);
}
