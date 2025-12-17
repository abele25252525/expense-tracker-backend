package com.example.expensetracker.controller;

import com.example.expensetracker.service.SummaryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/daily")
    public double daily(Authentication authentication,
                        @RequestParam String date) {

        return summaryService.dailyTotal(
                authentication.getName(),
                LocalDate.parse(date)
        );
    }

    @GetMapping("/monthly")
    public double monthly(Authentication authentication,
                          @RequestParam String month) {

        return summaryService.monthlyTotal(
                authentication.getName(),
                YearMonth.parse(month)
        );
    }
}
