package com.uottawa.seg2105.grp1.mealer.model;

import java.util.Map;

/**
 * Represents a User Entity in the Mealer app and Database.
 */
public final class User extends Entity {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String address;
    private boolean admin;
    private UserRole role;

    // Outward-facing getters.
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    protected String getPasswordHash() { return passwordHash; }
    public String getAddress() { return address; }
    public boolean isAdmin() { return admin; }
    public UserRole getRole() { return role; }

    /**
     * Searches persistent storage for a user with this email
     * @param email The email of the user to search for
     * @return The User with this email, or null if no such user exists in storage
     */
    public static User getByEmail(String email) {
        // TODO: implement User.getByEmail
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getId() {
        return email;
    }

    @Override
    public Map<String, Object> serialise() {
        // TODO: implement User.serialise()
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean deserialise(Map<String, Object> map) {
        // TODO: implement User.deserialise()
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
