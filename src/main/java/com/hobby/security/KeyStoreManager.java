package com.hobby.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Enumeration;

@Component
@Slf4j
public class KeyStoreManager {
    private KeyStore keyStore;

    @Value("${security.keystore.type}")
    private String type;
    @Value("${security.keystore.file-location}")
    private String fileLocation;
    @Value("${security.keystore.password}")
    private String password;

    @PostConstruct
    void init() {
        try {
            keyStore = KeyStore.getInstance(type);
            InputStream keyStoreFile = KeyStoreManager.class.getResourceAsStream(fileLocation);
            keyStore.load(keyStoreFile, password.toCharArray());
            log.info("Loaded keystore {} ", fileLocation);
        }
        catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Exception occurred while loading keystore ", e);
        }

    }
    PrivateKey getPrivateKey(String alias, String passphrase) {
        try {
            Enumeration<String> d = keyStore.aliases();
            while(d.hasMoreElements()) {
                log.info("Found alias " + d.nextElement());
            }
            if(keyStore.containsAlias(alias))
               log.info("Keystore contains alias");
            else
                log.info("Keystore does not contains alias");
            return (PrivateKey) keyStore.getKey(alias, passphrase.toCharArray());
        }
        catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException("Exception occured while retrieving public key from keystore", e);
        }
    }
    PublicKey getPublicKey(String alias) {
        try {
            return keyStore.getCertificate(alias).getPublicKey();
        }
        catch (KeyStoreException e) {
            throw new RuntimeException("Exception occured while retrieving public key from keystore", e);
        }
    }
}
