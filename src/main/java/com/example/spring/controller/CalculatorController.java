package com.example.spring.controller;

import com.example.spring.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping("/add")
    public ResponseEntity<CalculatorResponse> add(
            @RequestParam(value = "a", defaultValue = "0") int a,
            @RequestParam(value = "b", defaultValue = "0") int b) {
        try {
        	System.out.println("================add===================started");
            int result = calculatorService.performAddition(a, b);
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", result, calculatorService.getAccumulatedTotal(),
                    "Added " + a + " + " + b + " = " + result));
            
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.badRequest().body(new CalculatorResponse(
                    "Error", 0, 0, e.getMessage()));
        }
    }

    @GetMapping("/total")
    public ResponseEntity<CalculatorResponse> getTotal() {
        try {
            int total = calculatorService.getAccumulatedTotal();
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", total, total, "Current accumulated total: " + total));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CalculatorResponse(
                    "Error", 0, 0, e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<CalculatorResponse> reset() {
        try {
            calculatorService.resetCalculator();
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", 0, 0, "Calculator reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CalculatorResponse(
                    "Error", 0, 0, e.getMessage()));
        }
    }

    public static class CalculatorResponse {
        public String status;
        public int result;
        public int total;
        public String message;

        public CalculatorResponse(String status, int result, int total, String message) {
            this.status = status;
            this.result = result;
            this.total = total;
            this.message = message;
        }
    }
}
