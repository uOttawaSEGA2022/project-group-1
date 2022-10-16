package com.uottawa.seg2105.grp1.mealer.model;

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
     * @return The description of the cook
     */
    public String getDescription() { return description; }

    /**
     * Updates the cook's description.
     * @param description The new description.
     */
    public void setDescription(String description) {
        // TODO: Call repository to update in storage
        this.description = description;
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Map<String, Object> serialise() {
        // TODO: implement CookRole.serialise();
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        // TODO: implement CookRole.deserialise()
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
