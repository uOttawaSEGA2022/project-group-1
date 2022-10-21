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
     * @return The description of the cook
     */
    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    /**
     * Updates the cook's description.
     * @param description The new description.
     */
    public void setDescription(String description) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // Prepare new data
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    Map<String, Object> data = new HashMap<>();
                    data.put("description", description);

                    // Create the properties map
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("role", data);

                    // Update the role data
                    String id = getUser().getId();
                    rep.update(User.class, id, properties);

                    CookRole.this.description = description;
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("description",getDescription());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {

        Object desc = map.get("description");

        if (desc == null)
            throw new EntityDeserialisationException();

        this.description = (String) desc;
    }
}
