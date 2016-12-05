/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

 
/**
 * this class will create a UserInfo object and place it in the Principal
 * object available in controllers.
 * 
 * See {@link HomeController} for an example of extracting it.
 * 
 * The set up is in GoogleCalApApplication
 * @author dhenton
 */
public class GoogleUserInfoPrincipalExtractor implements PrincipalExtractor {

    
    
    private static final Logger LOG = LoggerFactory.getLogger(GoogleUserInfoPrincipalExtractor.class);
    @Override
    public Object extractPrincipal(Map<String, Object> map) {
         
         String name = (String) map.get("name");
         String id = (String) map.get("id");
         String givenName = (String) map.get("given_name");
         String familyName = (String) map.get("family_name");
         String gender = (String) map.get("gender");
         String picture = (String) map.get("picture");
         String link = (String) map.get("link");
         UserInfo u = new UserInfo(id,name,givenName,familyName,gender,picture,link); 
//         for (String k: map.keySet())
//         {
//             
//             LOG.info("key "+k+" "+map.get(k));
//             
//         }
        
         return u;
       
    }
    
}
