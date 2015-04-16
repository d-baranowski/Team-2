package com.team.two.lloyds_app.exceptions;

/**
 * Author: Daniel Baranowski
 * Date: 16/04/15
 * Purpose: Throws an exception relating to account number
 */
public class InvalidSortCodeFormatException extends Exception {
    public InvalidSortCodeFormatException() {
        super("Wrong account number format");
    }
}
