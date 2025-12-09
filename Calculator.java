import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private JTextField expression;
    private String operator = "";
    private double first = 0;
    private boolean start = true;

    public Calculator() {
        // Expression (small top field)
        expression = new JTextField();
        expression.setEditable(false);
        expression.setHorizontalAlignment(JTextField.LEFT);
        expression.setBorder(null);

        // Main display
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setText("");

        // Buttons panel (4x4)
        JPanel panel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };
        for (String text : buttons) {
            JButton b = new JButton(text);
            b.addActionListener(this);
            panel.add(b);
        }

        // Clear button
        JButton clear = new JButton("C");
        clear.addActionListener(e -> {
            display.setText("");
            expression.setText("");
            first = 0;
            operator = "";
            start = true;
        });

        // Top container for expression + display
        JPanel north = new JPanel(new BorderLayout());
        north.add(expression, BorderLayout.NORTH);
        north.add(display, BorderLayout.SOUTH);

        // Layout assembly
        add(north, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(clear, BorderLayout.SOUTH);

        setTitle("GUI Calculator");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();

        // Digit or dot
        if ("0123456789.".contains(input)) {
            if (input.equals(".") && display.getText().contains(".")) {
                return; // prevent multiple dots
            }
            display.setText(display.getText() + input);
            return;
        }

        // Equals
        if (input.equals("=")) {
            if (display.getText().isEmpty() || operator.isEmpty()) {
                return;
            }
            double second;
            try {
                second = Double.parseDouble(display.getText());
            } catch (NumberFormatException ex) {
                display.setText("Error");
                expression.setText("");
                operator = "";
                return;
            }

            double result = 0;
            switch (operator) {
                case "+": result = first + second; break;
                case "-": result = first - second; break;
                case "*": result = first * second; break;
                case "/":
                    if (second == 0) {
                        display.setText("Error");
                        expression.setText("");
                        operator = "";
                        return;
                    }
                    result = first / second;
                    break;
            }

            expression.setText(first + " " + operator + " " + second + " =");
            display.setText(String.valueOf(result));
            operator = "";
            start = true;
            return;
        }

        // Operator pressed (+ - * /)
        // If there's already a pending operator and user entered a new number, compute it first
        if (!operator.isEmpty() && !display.getText().isEmpty()) {
            double second;
            try {
                second = Double.parseDouble(display.getText());
            } catch (NumberFormatException ex) {
                display.setText("Error");
                expression.setText("");
                operator = "";
                return;
            }

            switch (operator) {
                case "+": first = first + second; break;
                case "-": first = first - second; break;
                case "*": first = first * second; break;
                case "/":
                    if (second == 0) {
                        display.setText("Error");
                        expression.setText("");
                        operator = "";
                        return;
                    }
                    first = first / second;
                    break;
            }
            display.setText("");
        } else if (!display.getText().isEmpty()) {
            try {
                first = Double.parseDouble(display.getText());
            } catch (NumberFormatException ex) {
                display.setText("Error");
                expression.setText("");
                operator = "";
                return;
            }
            display.setText("");
        }

        operator = input;
        expression.setText(first + " " + operator);
        start = false;
    }

    public static void main(String[] args) {
        // Launch on Event Dispatch Thread
        SwingUtilities.invokeLater(Calculator::new);
    }
}
