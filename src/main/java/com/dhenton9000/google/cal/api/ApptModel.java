/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api;

/**
 *
 * @author dhenton
 */
public class ApptModel {
    
    private String toField ;
    private String fromField;
    private String textField ;

    /**
     * @return the toField
     */
    public String getToField() {
        return toField;
    }

    /**
     * @param toField the toField to set
     */
    public void setToField(String toField) {
        this.toField = toField;
    }

    /**
     * @return the fromField
     */
    public String getFromField() {
        return fromField;
    }

    /**
     * @param fromField the fromField to set
     */
    public void setFromField(String fromField) {
        this.fromField = fromField;
    }

    /**
     * @return the textField
     */
    public String getTextField() {
        return textField;
    }

    /**
     * @param textField the textField to set
     */
    public void setTextField(String textField) {
        this.textField = textField;
    }

    @Override
    public String toString() {
        return "ApptModel{" + "toField=" + toField + ", fromField=" + fromField + ", textField=" + textField + '}';
    }
    
    
}
