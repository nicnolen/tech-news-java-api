package com.technews;

public class VariablesPractice {
    public static void main(String[] args) {
        // declare variables
        int num1 = 69;
        double num2 = 100.356;
        String string1 = "cat";

        // calculated variable sum is a double
        double sum = num1 + num2;

        // calculated variable concat is a String
        String concat = num1 + string1;

        // value of sum 169.356
        System.out.println(sum);

        // value of concat 69cat
        System.out.println(concat);
    }
}
