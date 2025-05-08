package com.kewen.framework.idaas.application.config;

import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.crypto.KeySupport;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class IDPCredentials {
    private static final Credential credential;

    static {
        credential = generateCredential();
    }

    private static Credential generateCredential() {
        try {
            KeyPair keyPair = KeySupport.generateKeyPair("RSA", 1024, null);
            return CredentialSupport.getSimpleCredential(keyPair.getPublic(), keyPair.getPrivate());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static Credential getCredential() {
        return credential;
    }

}
