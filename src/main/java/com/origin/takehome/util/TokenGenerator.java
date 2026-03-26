package com.origin.takehome.util;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    @SuppressWarnings("unused")
    private void TokenGernerator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static String token() {
        return RandomStringUtils.insecure().nextAlphanumeric(6);
    }
}
