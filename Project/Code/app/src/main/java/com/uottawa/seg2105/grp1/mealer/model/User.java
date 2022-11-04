package com.uottawa.seg2105.grp1.mealer.model;

import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;
import com.uottawa.seg2105.grp1.mealer.ui.LoginPage;

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
        IRepository rep = MealerSystem.getSystem().getRepository();
        User user = null;
        try {
            user = rep.getById(User.class, email);
        } catch (RepositoryRequestException e) {}
        return user;
    }

    /**
     * Creates a new user and adds them to the repository.
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param address
     * @param role
     * @return the created user after it has been saved in the repository
     * @exception RepositoryRequestException if the repository request failed
     * @exception UserAlreadyExistsException if the email is already taken
     */
    public static User createNewUser(String firstName, String lastName, String email, String password, String address, UserRole role, boolean overwriteIfExists) throws RepositoryRequestException, UserAlreadyExistsException {
        User user = new User();

        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.passwordHash = MealerSystem.getSystem().hashPassword(password);
        user.address = address;
        user.admin = role == null;
        user.role = role;

        if (role != null)
            role.user = user;

        // Create or overwrite the User in the repository.
        IRepository rep = MealerSystem.getSystem().getRepository();

        if (!overwriteIfExists && rep.hasId(User.class, email))
            throw new UserAlreadyExistsException();
        rep.set(User.class, user);

        return user;
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
        if (getRole() != null)
            map.put("role", getRole().serialise());

        if (getRole() instanceof ClientRole) {
            map.put("roleType", "Client");
        } else if (getRole() instanceof CookRole) {
            map.put("roleType", "Cook");
        } else {
            map.put("roleType", "Admin");
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

        if (firstName == null || lastName == null || email == null    || passwordHash == null
           || address == null ||    admin == null || roleType == null || ( !roleType.equals("Admin") && role == null)) {
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
