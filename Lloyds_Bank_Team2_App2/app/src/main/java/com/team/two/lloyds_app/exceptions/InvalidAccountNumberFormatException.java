package com.team.two.lloyds_app.exceptions;

/**
 * Created by danielbaranowski on 16/04/15.
 */
public class InvalidAccountNumberFormatException extends Exception {
    public InvalidAccountNumberFormatException() {
        super("Wrong account number format");
    }
}
