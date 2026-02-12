package com.example.spring.config;

import com.example.ejb.calculator.CalculatorRemote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import java.util.Properties;

/**
 * Spring configuration for EJB integration via JNDI.
 * Configures JndiTemplate and JndiObjectFactoryBean (proxy) for remote EJB lookup (WildFly).
 */
@Configuration
public class EJBIntegrationConfig {

    @Value("${jndi.initialContextFactory:org.wildfly.naming.client.WildFlyInitialContextFactory}")
    private String initialContextFactory;

    @Value("${jndi.providerUrl:http-remoting://localhost:8080}")
    private String providerUrl;

    @Value("${jndi.securityPrincipal:}")
    private String securityPrincipal;

    @Value("${jndi.securityCredential:}")
    private String securityCredential;

    @Value("${jndi.calculatorLookup:ejb:calculator-ejb/calculator-ejb/CalculatorBean!com.example.ejb.calculator.CalculatorRemote}")
    private String calculatorJndiName;

    @Bean
    public JndiTemplate jndiTemplate() {
        Properties env = new Properties();
        env.setProperty("java.naming.factory.initial", initialContextFactory);
        env.setProperty("java.naming.provider.url", providerUrl);
        if (securityPrincipal != null && !securityPrincipal.isEmpty()) {
            env.setProperty("java.naming.security.principal", securityPrincipal);
        }
        if (securityCredential != null && !securityCredential.isEmpty()) {
            env.setProperty("java.naming.security.credentials", securityCredential);
        }
        return new JndiTemplate(env);
    }

    /**
     * Spring JNDI Proxy: JndiObjectFactoryBean performs JNDI lookup at startup
     * and exposes CalculatorRemote as a Spring-managed bean for injection.
     * Use @Qualifier("calculatorEjbProxy") to inject this proxy in services.
     */
   @Bean(name = "calculatorEjbProxy")
    public CalculatorRemote calculatorEjbProxy(JndiTemplate jndiTemplate) throws Exception {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiTemplate(jndiTemplate);
        factory.setJndiName(calculatorJndiName);
        factory.setProxyInterface(CalculatorRemote.class);
        factory.setLookupOnStartup(true);
        factory.setCache(true);
        factory.afterPropertiesSet();
        return (CalculatorRemote) factory.getObject();
    }
}
