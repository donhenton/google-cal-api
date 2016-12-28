/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.drive;

/**
 *
 * @author dhenton
 */
public class FileReturnInfo {
    @com.google.api.client.util.Key
    private String id;
    @com.google.api.client.util.Key
    private String kind;
    @com.google.api.client.util.Key
    private String name;
    @com.google.api.client.util.Key
    private String mimeType;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "FileReturnInfo{" + "id=" + id + ", kind=" + kind + ", name=" + name + ", mimeType=" + mimeType + '}';
    }
    
    
    
    
}
