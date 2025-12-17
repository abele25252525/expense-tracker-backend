package com.example.expensetracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    private String id;

    private String userEmail;

    // TOTAL BUDGET AMOUNT
    private double amount;

    // WEEK / MONTH / YEAR
    private String period;

    private LocalDate startDate;
    private LocalDate endDate;
}
