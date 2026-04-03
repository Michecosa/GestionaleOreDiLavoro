package com.workinghoursmanagement.utility;

import java.util.Scanner;

public class Utility {
    
    // Chiede un input integer e verifica validità
    @SuppressWarnings("resource")
    public static int askInt() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String input = scanner.nextLine();
                int value = Integer.parseInt(input); // Converte stringa in int
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Errore: inserisci un numero intero valido.");
            }
        }
    }

    // Chiede un input double e verifica validità
    @SuppressWarnings("resource")
    public static double askDouble() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String input = scanner.nextLine();
                double value = Double.parseDouble(input); // Converte stringa in double
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Errore: inserisci un numero decimale valido.");
            }
        }
    }

    // Chiede un input float e verifica validità
    @SuppressWarnings("resource")
    public static float askFloat() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String input = scanner.nextLine();
                float value = Float.parseFloat(input); // Converte stringa in float
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Errore: inserisci un numero decimale valido.");
            }
        }
    }

    // Chiede un input boolean e verifica validità (accetta true/false, s/n, y/n)
    @SuppressWarnings("resource")
    public static boolean askBoolean() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("true") || input.equals("t") || input.equals("si") || input.equals("s") || input.equals("y") || input.equals("yes")) {
                return true;
            } 
            else if (input.equals("false") || input.equals("f") || input.equals("no") || input.equals("n")) {
                return false;
            } 
            else {
                System.out.println("Errore: inserisci true/false oppure s/n.");
            }
        }
    }

    // Chiede un input stringa (nessun controllo sul tipo)
    @SuppressWarnings("resource")
    public static String askString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}