package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {
    public static void main(String[] args) {
        String input = args[0];

        ObjectStack stack = new ObjectStack();
        for (String s : input.split(" ")) {
            try {
                int num = Integer.parseInt(s);
                stack.push(num);
            } catch (NumberFormatException e) {
                int op_b = (Integer) stack.pop();
                int op_a = (Integer) stack.pop();
                switch (s.strip()) {
                    case "+":
                        stack.push(op_a + op_b);
                        break;

                    case "-":
                        stack.push(op_a - op_b);
                        break;

                    case "*":
                        stack.push(op_a * op_b);
                        break;

                    case "/":
                        if (op_b == 0) {
                            throw new ArithmeticException("Division by zero.");
                        }
                        stack.push(op_a / op_b);
                        break;
                    default:
                        System.out.printf("%s is not an operator.", s);
                }
            }
        }
        
        if (stack.size() != 1) {
            System.out.println("error");
        } else {
            System.out.println(stack.pop());
        }
    }
}