package com.example.spring.controller;

import com.example.spring.service.CalculatorProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Calculator operations via Spring JNDI Proxy.
 * Uses CalculatorProxyService (JndiObjectFactoryBean-proxied EJB).
 */
@RestController
@RequestMapping("/api/calculator/proxy")
public class CalculatorProxyController {

    @Autowired
    private CalculatorProxyService calculatorProxyService;

    @GetMapping("/add")
    public ResponseEntity<CalculatorResponse> add(
            @RequestParam(value = "a", defaultValue = "0") int a,
            @RequestParam(value = "b", defaultValue = "0") int b) {
        try {
            int result = calculatorProxyService.performAddition(a, b);
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", result, calculatorProxyService.getAccumulatedTotal(),
                    "Added " + a + " + " + b + " = " + result + " (via JNDI Proxy)"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CalculatorResponse(
                    "Error", 0, 0, e.getMessage()));
        }
    }

    @GetMapping("/total")
    public ResponseEntity<CalculatorResponse> getTotal() {
        try {
            int total = calculatorProxyService.getAccumulatedTotal();
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", total, total,
                    "Current accumulated total: " + total + " (via JNDI Proxy)"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CalculatorResponse(
                    "Error", 0, 0, e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<CalculatorResponse> reset() {
        try {
            calculatorProxyService.resetCalculator();
            return ResponseEntity.ok(new CalculatorResponse(
                    "Success", 0, 0, "Calculator reset successfully (via JNDI Proxy)"));
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
