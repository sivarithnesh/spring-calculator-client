package com.example.spring.servlet;

import com.example.ejb.calculator.CalculatorRemote;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that invokes Calculator EJB via @EJB injection.
 * Works when deployed to WildFly (same server as the EJB).
 */
@WebServlet(urlPatterns = "/api/calculator/ejb/*")
public class CalculatorEjbServlet extends HttpServlet {

    // Same-server lookup when WAR and EJB are deployed to the same WildFly.
    // Adjust app/module names to match your deployment (check server log for JNDI binding).
    //@EJB(lookup = "java:global/ejb:/calculator-ejb-1.0.0/CalculatorBean!com.example.ejb.calculator.CalculatorRemote?stateful")
	//@EJB(lookup = "java:global/calculator-ejb-1.0.0/CalculatorBean!com.example.ejb.calculator.CalculatorRemote")
	//private CalculatorRemote calculatorRemote;

	@EJB
	private CalculatorRemote calculatorRemote;
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            if (calculatorRemote == null) {
                sendError(out, "EJB not injected. Deploy this WAR to WildFly (same server as calculator-ejb).");
                return;
            }

            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(out, "Use /api/calculator/ejb/add, /api/calculator/ejb/total");
                return;
            }

            switch (pathInfo) {
                case "/add" -> handleAdd(req, out);
                case "/total" -> handleTotal(out);
                default -> sendError(out, "Unknown path: " + pathInfo);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            if (calculatorRemote == null) {
                sendError(out, "EJB not injected. Deploy this WAR to WildFly.");
                return;
            }

            if ("/reset".equals(pathInfo)) {
                calculatorRemote.reset();
                sendSuccess(out, 0, 0, "Calculator reset successfully (via @EJB)");
            } else {
                sendError(out, "Unknown path: " + pathInfo);
            }
        }
    }

    private void handleAdd(HttpServletRequest req, PrintWriter out) {
        int a = parseInt(req.getParameter("a"), 0);
        int b = parseInt(req.getParameter("b"), 0);
        int result = calculatorRemote.add(a, b);
        int total = calculatorRemote.getTotal();
        sendSuccess(out, result, total, "Added " + a + " + " + b + " = " + result + " (via @EJB)");
    }

    private void handleTotal(PrintWriter out) {
        int total = calculatorRemote.getTotal();
        sendSuccess(out, total, total, "Current accumulated total: " + total + " (via @EJB)");
    }

    private void sendSuccess(PrintWriter out, int result, int total, String message) {
        out.print("{\"status\":\"Success\",\"result\":" + result + ",\"total\":" + total + ",\"message\":\"" + escape(message) + "\"}");
    }

    private void sendError(PrintWriter out, String message) {
        out.print("{\"status\":\"Error\",\"result\":0,\"total\":0,\"message\":\"" + escape(message) + "\"}");
    }

    private static int parseInt(String s, int def) {
        if (s == null || s.isBlank()) return def;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
