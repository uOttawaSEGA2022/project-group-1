package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores extra information about Cook Users.
 */
public class CookRole extends UserRole {
    /**
     * The short description set by the cook
     */
    private String description;

    /**
     * Ban expiration date
     * -1: not banned
     *  0: permabanned
     *  else: UTC timestamp
     */
    private long banExpiration;

    /**
     * Reason for ban if banned else empty string
     */
    private String banReason;

    public CookRole() {
        this.banExpiration = -1;
        this.banReason = "";
    }

    /**
     * @return The description of the cook
     */
    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    public long getBanExpiration() {
        return banExpiration;
    }

    public String getBanReason() {
        if (banReason == null)
            return "";
        return banReason;
    }

    /**
     * Updates the cook's role data
     * @param desc The new description.
     * @param exp The new banExpiration
     * @param reason The new banReason
     * @throws RepositoryRequestException if data could not be updated
     */
    public void setRole(String desc, long exp, String reason) throws RepositoryRequestException {
        // Prepare new data
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> data = new HashMap<>();
        data.put("description", desc);
        data.put("banExpiration", exp);
        data.put("banReason", reason);

        // Create the properties map
        Map<String, Object> properties = new HashMap<>();
        properties.put("role", data);

        // Update the role data
        String id = getUser().getId();
        rep.update(User.class, id, properties);

        CookRole.this.description = desc;
        CookRole.this.banExpiration = exp;
        CookRole.this.banReason = reason;
    }
    /**
     * Updates the cook's description.
     * @param description The new description.
     */
    public void setDescription(String description) throws RepositoryRequestException {
        setRole(description, this.banExpiration, this.banReason);
    }
    /**
     * Updates the cook's ban information
     * @param exp The new banExpiration
     * @param reason The new banReason
     */
    public void setBan(long exp, String reason) throws RepositoryRequestException {
        setRole(this.description, exp, reason);
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("description",getDescription());
        map.put("banExpiration", getBanExpiration());
        map.put("banReason", getBanReason());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {

        Object desc = map.get("description");
        Object exp = map.get("banExpiration");
        Object reason = map.get("banReason");

        if (desc == null || exp == null || reason == null)
            throw new EntityDeserialisationException();

        this.description = (String) desc;
        this.banExpiration = (long) exp;
        this.banReason = (String) reason;
    }
}
