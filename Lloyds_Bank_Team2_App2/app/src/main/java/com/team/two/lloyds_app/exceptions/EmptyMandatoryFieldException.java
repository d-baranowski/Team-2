package com.team.two.lloyds_app.Exceptions;

/**
 * Created by danielbaranowski on 29/03/15.
 */
public class EmptyMandatoryFieldException extends Exception {
    public EmptyMandatoryFieldException(String fieldName) {
        super(fieldName+" can't be empty");
    }
}
