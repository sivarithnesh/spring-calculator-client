package com.example.spring.service;

import com.example.ejb.calculator.CalculatorRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;

/**
 * Spring service that invokes the Calculator EJB via JNDI lookup.
 */
@Service
public class CalculatorService {

    @Autowired
    private org.springframework.jndi.JndiTemplate jndiTemplate;

    @Value("${jndi.calculatorLookup:ejb:/calculator-ejb-1.0.0/CalculatorBean!com.example.ejb.calculator.CalculatorRemote?stateful}")
    private String calculatorJndiName;

    private volatile CalculatorRemote calculatorEJB;

    private void ensureCalculator() {
    	 
        if (calculatorEJB == null) {
            synchronized (this) {
                if (calculatorEJB == null) {
                    try {
                        calculatorEJB = (CalculatorRemote) jndiTemplate.lookup(calculatorJndiName);
                    	System.out.println("================ensureCalculator  	 calculatorEJB::" + calculatorEJB.toString());
                    } catch (NamingException e) {
                        throw new RuntimeException("Failed to lookup Calculator EJB from JNDI: " + calculatorJndiName, e);
                    }
                }
            }
        }
    }

    public int performAddition(int a, int b) {
        ensureCalculator();
        return calculatorEJB.add(a, b);
    }

    public int getAccumulatedTotal() {
        ensureCalculator();
        return calculatorEJB.getTotal();
    }

    public void resetCalculator() {
        ensureCalculator();
        calculatorEJB.reset();
    }
}
