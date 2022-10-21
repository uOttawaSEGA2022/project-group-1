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
        // TODO: Call repository to update in storage
        this.description = description;
        String email = this.user.getEmail();
        //throw new UnsupportedOperationException("Not implemented yet.");

        //added here
        new Thread() {
            @Override
            public void run() {
                try {
                    // Create or overwrite the User in the repository.
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    Map<String, Object> data = new HashMap<>();
                    data.put("description", description);
                    rep.update(User.class, email, data);
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
