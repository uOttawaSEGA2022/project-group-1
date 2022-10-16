package com.uottawa.seg2105.grp1.mealer.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.CloudFirestoreRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * The System in charge of maintaining application state.
 *
 * Must be initialized
 */
public final class System extends Application {
    private static final String SP_NAME = "usersession";
    private static final String SP_USERNAME_FIELD = "username";

    // The user session sharedpref
    private SharedPreferences usersession;

    // The current user, or null if no User is logged in.
    private User currentUser;

    // The repository used to fetch permanent data
    private IRepository repository;

    /**
     * @return The current logged-in user, or nil if there is none
     */
    public User getCurrentUser() { return currentUser; }

    /**
     * @return The global repository object
     */
    public IRepository getRepository() { return repository; }

    /**
     * When the Application is started, this method restores the user's current session, if any.
     * It also creates the repository object
     */
    @Override
    public void onCreate() {
        super.onCreate();
        usersession = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        repository = new CloudFirestoreRepository();

        String username = usersession.getString(SP_USERNAME_FIELD, null);
        if (username == null)
            logoff();
        else {
            try {
                User user = repository.getById(User.class, username);
                login(user);
            } catch (RepositoryRequestException e) {
                logoff();
            }
        }
    }

    /**
     * Terminates the current user session.
     * Does nothing if there is no current session.
     */
    public void logoff() {
        Editor edit = usersession.edit();

        edit.remove(SP_USERNAME_FIELD);
        edit.apply();

        currentUser = null;
    }

    /**
     * Attempts to create a user session using user credentials
     * @param username The email of the user to log in as
     * @param password The password of the user to lig in as
     * @return true if login was successful, false otherwise.
     */
    public boolean tryLogin(String username, String password) {
        try {
            User user = repository.getById(User.class, username);
            if (user == null)
                return false;

            if (!user.getPasswordHash().equals(hashPassword(password)))
                return false;

            login(user);
            return true;

        } catch (RepositoryRequestException e) {
            return false;
        }
    }

    /**
     * Registers a User entity as the current user of this session
     * @param user The User entity the session belongs to
     */
    private void login(User user) {
        Editor edit = usersession.edit();

        edit.putString(SP_USERNAME_FIELD, user.getEmail());
        edit.apply();

        currentUser = user;
    }

    /**
     * Hashes a password using SHA-256 on the UTF-8 byte encoding.
     * Returns a lowercase hex string.
     *
     * @param password the password
     * @return the lowercase hex string SHA-256 on UTF-8
     */
    private String hashPassword(String password) {
        try {
            // Digest using SHA-256 on UTF-8 encoding
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert to lowercase hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(b);
                if (hex.length() < 2)
                    sb.append("0");
                sb.append(hex);
            }

            return sb.toString().toLowerCase();

        } catch (Exception e) {
            throw new RuntimeException(e); // we can't continue if an error occurs
        }

    }
}
