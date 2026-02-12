package com.example.spring.service;

import com.example.ejb.calculator.CalculatorRemote;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Spring service that invokes the Calculator EJB via Spring JNDI Proxy.
 * Uses JndiObjectFactoryBean-proxied CalculatorRemote injected at startup.
 */
@Service
public class CalculatorProxyService {

    private final CalculatorRemote calculatorEjbProxy;

 public CalculatorProxyService(@Qualifier("calculatorEjbProxy") CalculatorRemote calculatorEjbProxy) {
        this.calculatorEjbProxy = calculatorEjbProxy;
    }
 

    public int performAddition(int a, int b) {
    	System.out.println("Proxy way: calculatorEjbProxy:::"+calculatorEjbProxy);
        return calculatorEjbProxy.add(a, b);
    }

    public int getAccumulatedTotal() {
        return calculatorEjbProxy.getTotal();
    }

    public void resetCalculator() {
        calculatorEjbProxy.reset();
    }
}
