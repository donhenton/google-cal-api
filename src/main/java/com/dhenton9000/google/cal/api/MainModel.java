/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api;

import java.util.Properties;

/**
 *
 * @author dhenton
 */
public class MainModel {

    private String firstAttendee;
    private String secondAttendee;
    private String fileName = "test";
    private String dateField;
    private String text = "";
    private String linkValue = "http://www.yahoo.com";
    private String linkText = "Yahoo!!!";

    public Properties getProperties() {
        Properties replacementProps = new Properties();
        replacementProps.setProperty("ATTENDEES", getFirstAttendee()
                +","+getSecondAttendee());         
        replacementProps.setProperty("DATE_FIELD", getDateField());
        replacementProps.setProperty("TEXT_FIELD",  getText());
        replacementProps.setProperty("HYPERLINK_HREF", getLinkValue());
        replacementProps.setProperty("HYPERLINK_TEXT", getLinkText());

        return replacementProps;
    }

    /**
     * @return the firstAttendee
     */
    public String getFirstAttendee() {
        return firstAttendee;
    }

    /**
     * @param firstAttendee the firstAttendee to set
     */
    public void setFirstAttendee(String firstAttendee) {
        this.firstAttendee = firstAttendee;
    }

    /**
     * @return the secondAttendee
     */
    public String getSecondAttendee() {
        return secondAttendee;
    }

    /**
     * @param secondAttendee the secondAttendee to set
     */
    public void setSecondAttendee(String secondAttendee) {
        this.secondAttendee = secondAttendee;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the dateField
     */
    public String getDateField() {
        return dateField;
    }

    /**
     * @param dateField the dateField to set
     */
    public void setDateField(String dateField) {
        this.dateField = dateField;
    }

    /**
     * @return the linkValue
     */
    public String getLinkValue() {
        return linkValue;
    }

    /**
     * @param linkValue the linkValue to set
     */
    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    /**
     * @return the linkText
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * @param linkText the linkText to set
     */
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    @Override
    public String toString() {
        return "MainModel{" + "firstAttendee=" + firstAttendee + ", secondAttendee=" + secondAttendee + ", fileName=" + fileName + ", dateField=" + dateField + '}';
    }

}
