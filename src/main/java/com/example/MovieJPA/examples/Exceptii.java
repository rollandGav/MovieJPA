package com.example.MovieJPA.examples;

public class Exceptii {
    public static void main(String[] args) {

        try {
            int result = 10 / 0; // aritmetic E
            System.out.println("result is: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught AritmeticException: " + e.getMessage());
        }

        try {
            int[] numbers = {1,2,3};
            System.out.println(numbers[5]);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Caught ArrayIndexOutOfBoundsException: " + e.getMessage());
        }


        try {
            System.out.println("waiting for 1 sec");
            Thread.sleep(1000);
        } catch (InterruptedException | RuntimeException e) {
            throw new RuntimeException(e);
        }

        try {
            validateAge(20);
            validateAge(15);
        } catch (InvalidAgeException e ) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("it will run");
        }

    }

    public static void validateAge(int age) throws InvalidAgeException {
        if(age<18){
            throw new InvalidAgeException("age must be 18 or over");
        }
    }
}

class InvalidAgeException extends Exception{
    public InvalidAgeException(String message){
        super(message);
    }
}
