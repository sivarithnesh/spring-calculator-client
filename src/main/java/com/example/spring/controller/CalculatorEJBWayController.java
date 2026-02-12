package com.example.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.service.CalculatorEJBWayService;

@RestController
@RequestMapping("/api/calculator/ejbway")
public class CalculatorEJBWayController {

    @Autowired
    private CalculatorEJBWayService calculatorEJBWayService;

    @GetMapping("/add")
    public ResponseEntity<CalculatorResponse> add(
            @RequestParam(value = "a", defaultValue = "0") int a,
            @RequestParam(value = "b", defaultValue = "0") int b) {
        try {
        	System.out.println("================add===================started");
            int result = calculatorEJBWayService.performAddition(a, b);
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", result, calculatorEJBWayService.getAccumulatedTotal(),
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
            int total = calculatorEJBWayService.getAccumulatedTotal();
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
