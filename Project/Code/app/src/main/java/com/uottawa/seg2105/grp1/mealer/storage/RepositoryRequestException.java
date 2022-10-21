package com.uottawa.seg2105.grp1.mealer.storage;

/**
 * An exception thrown whenever an error occurs within a Repository request
 */
public class RepositoryRequestException extends Exception {
    public RepositoryRequestException() {
        super();
    }

    public RepositoryRequestException(String message) {
        super(message);
    }
}
