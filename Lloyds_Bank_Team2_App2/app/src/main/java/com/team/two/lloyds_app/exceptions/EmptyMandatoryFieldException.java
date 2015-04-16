package com.team.two.lloyds_app.exceptions;

/**
 * Author: Daniel Baranowski
 * Date: 16/04/15
 * Purpose: Throws an exception relating to a field that shouldn't be empty
 */
public class EmptyMandatoryFieldException extends Exception {
    public EmptyMandatoryFieldException(String fieldName) {
        super(fieldName+" can't be empty");
    }
}
