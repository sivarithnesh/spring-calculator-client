package com.example.spring.service;

import org.springframework.stereotype.Service;

import com.example.ejb.calculator.CalculatorRemote;

import jakarta.ejb.EJB;

/**
 * Spring service that invokes the Calculator EJB via @EJB Way.
 */
@Service
public class CalculatorEJBWayService {

    
	@EJB
	private CalculatorRemote calculatorRemote;
	

   
    public int performAddition(int a, int b) {
         
        return calculatorRemote.add(a, b);
    }

    public int getAccumulatedTotal() {
       
        return calculatorRemote.getTotal();
    }

    public void resetCalculator() {
        
        calculatorRemote.reset();
    }
}
