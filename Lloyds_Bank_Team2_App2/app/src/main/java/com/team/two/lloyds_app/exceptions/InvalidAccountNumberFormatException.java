package com.team.two.lloyds_app.exceptions;

/**
 * Author: Daniel Baranowski
 * Date: 16/04/15
 * Purpose: Throws an exception relating to account number
 */
public class InvalidAccountNumberFormatException extends Exception {
    public InvalidAccountNumberFormatException() {
        super("Wrong account number format");
    }
}
