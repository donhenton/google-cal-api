/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.drive;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dhenton
 */
public class GoogleDriveWriter {

    public static final String DOCX_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    
    
    
    /**
     * produce an entity to be uploaded to the Drive API.
     *
     * @param byteData the byte data to send
     * @param jsonMetaData the meta data in json format as a string
     * @param payloadContentType eg. 'image/png'
     * @return 
     */
    public HttpEntity<MultiValueMap<String, Object>>  produceEntity(byte[] byteData, String jsonMetaData, String payloadContentType) {

        //the parts of the upload
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        //the main header
        HttpHeaders mainHeaders = new HttpHeaders();
        mainHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        //the meta data
        MultiValueMap<String, String> metaHeaders = new LinkedMultiValueMap<String, String>();
        metaHeaders.set("Content-type", "application/json");

        HttpEntity metaEntity = new HttpEntity(jsonMetaData, metaHeaders);

        //the payload data
        MultiValueMap<String, String> payloadHeaders = new LinkedMultiValueMap<String, String>();
        payloadHeaders.set("Content-type", payloadContentType);
        Resource payloadResource = new ByteArrayResource(byteData);
        parts.add("metaData", metaEntity);
        parts.add("payload", payloadResource);
        HttpEntity binaryEntity = new HttpEntity(parts, mainHeaders);

        
        
        return binaryEntity;

    }
}
