package com.baculsoft.gcm.simple.utils;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public interface IConstants {
    /**
     * Shared Preferences
     */
    interface IPreferences {
        String GCM_TOKEN = "gcm_token";
        String GCM_TOKEN_STATUS = "gcm_token_status";
    }

    /**
     * GCM Keys
     */
    interface IGcm {
        String[] TOPICS = { "global" };

        String REGISTRATION_COMPLETE = "registration_complete";
    }
}