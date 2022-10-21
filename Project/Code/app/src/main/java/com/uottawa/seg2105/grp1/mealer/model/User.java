package com.uottawa.seg2105.grp1.mealer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a User Entity in the Mealer app and Database.
 */
public final class User implements IRepositoryEntity {
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

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("firstName",getFirstName());
        map.put("lastName",getLastName());
        map.put("email",getEmail());
        map.put("passwordHash",getPasswordHash());
        map.put("address",getAddress());
        map.put("admin",isAdmin());
        map.put("role",getRole().serialise());

        if (getRole() instanceof ClientRole) {
            map.put("roleType", "Client");
        } else if (getRole() instanceof CookRole) {
            map.put("roleType", "Cook");
        }

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {

        Object firstName = map.get("firstName");
        Object lastName = map.get("lastName");
        Object email = map.get("email");
        Object passwordHash = map.get("passwordHash");
        Object address = map.get("address");
        Object admin = map.get("admin");
        Object role = map.get("role");
        Object roleType = map.get("roleType");

        if (firstName == null || lastName == null || email == null || passwordHash == null
           || address == null ||    admin == null ||  role == null ||     roleType == null) {
            throw new EntityDeserialisationException();
        }
        this.firstName = (String) firstName;
        this.lastName = (String) lastName;
        this.email = (String) email;
        this.passwordHash = (String) passwordHash;
        this.address = (String) address;
        this.admin = (boolean) admin;

        if (roleType.toString().equals("Client")) {
            ClientRole c = new ClientRole();
            c.deserialise((Map<String, Object>) role);
            this.role = c;
        } else if (roleType.toString().equals("Cook")) {
            CookRole c = new CookRole();
            c.deserialise((Map<String, Object>) role);
            this.role = c;
        }
    }
}
