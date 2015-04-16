package com.team.two.lloyds_app.exceptions;

/**
 * Created by danielbaranowski on 16/04/15.
 */
public class InvalidSortCodeFormatException extends Exception {
    public InvalidSortCodeFormatException() {
        super("Wrong account number format");
    }
}
