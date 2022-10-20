package com.uottawa.seg2105.grp1.mealer.model;

import java.util.Map;

/**
 * Represents a User Entity in the Mealer app and Database.
 */
public final class User implements IDatabaseEntity {
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
    String getPasswordHash() { return passwordHash; }
    public String getAddress() { return address; }
    public boolean isAdmin() { return admin; }
    public UserRole getRole() { return role; }

    // Defines the storage table name for this class.
    static { IDatabaseEntity.tableNames.put(User.class, "users"); }

    /**
     * Searches persistent storage for a user with this email
     * @param email The email of the user to search for
     * @return The User with this email, or null if no such user exists in storage
     */
    public static User getByEmail(String email) {
        // TODO: implement User.getByEmail that searches through database
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
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        // TODO: implement User.deserialise()
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
