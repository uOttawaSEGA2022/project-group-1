package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * Sum of the values (1-5) of all ratings received
     */
    private long totalRatings;

    /**
     * Number of ratings received
     */
    private long numRatings;

    /**
     * Number of meals sold
     */
    private long mealsSold;

    public CookRole() {
        this.banExpiration = -1;
        this.banReason = "";
        this.totalRatings = 0;
        this.numRatings = 0;
        this.mealsSold = 0;
    }

    /**
     * Creates the cook's role data
     * @param desc The new description.
     * @throws RepositoryRequestException if data could not be updated
     */
    public void create(String desc) throws RepositoryRequestException {
        // Prepare new data
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> data = new HashMap<>();
        data.put("description", desc);
        data.put("banExpiration", this.banExpiration);
        data.put("banReason", this.banReason);
        data.put("totalRatings", this.totalRatings);
        data.put("numRatings", this.numRatings);
        data.put("mealsSold", this.mealsSold);

        // Create the properties map
        Map<String, Object> properties = new HashMap<>();
        properties.put("role", data);

        // Update the role data
        String id = getUser().getId();
        rep.update(User.class, id, properties);

        this.description = desc;
    }

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

    public float getAverageRating() {
        if (numRatings == 0)
            return 0;
        return (float) totalRatings/numRatings;
    }

    public List<PurchaseRequest> getPurchaseRequests() throws RepositoryRequestException {
        List<PurchaseRequest> purchases;
        String email = this.getUser().getEmail();
        IRepository rep = MealerSystem.getSystem().getRepository();
        purchases = rep.query(PurchaseRequest.class,
                (pr) -> pr.getCook().getEmail().equals(email)
        );
        return purchases;
    }


    /**
     * Increments the number of ratings and adds this rating to the total
     * @param rating
     * @throws RepositoryRequestException
     */
    public void rate(int rating) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> properties = new HashMap<>();
        properties.put("role.numRatings", this.numRatings + 1);
        properties.put("role.totalRatings", this.totalRatings + rating);
        String id = getUser().getId();
        rep.update(User.class, id, properties);
        this.numRatings = this.numRatings + 1;
        this.totalRatings = this.totalRatings + rating;
    }

    /**
     * Increments the number of meals sold by the cook
     * @throws RepositoryRequestException
     */
    public void soldMeal() throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> properties = new HashMap<>();
        properties.put("role.mealsSold", this.mealsSold + 1);
        String id = getUser().getId();
        rep.update(User.class, id, properties);
        this.mealsSold = this.mealsSold + 1;
    }

    public long getMealsSold() { return mealsSold; }

    /**
     * Updates the cook's description.
     * @param description The new description.
     */
    public void setDescription(String description) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> properties = new HashMap<>();
        properties.put("role.description", description);
        String id = getUser().getId();
        rep.update(User.class, id, properties);
        this.description = description;
    }
    /**
     * Updates the cook's ban information
     * @param exp The new banExpiration
     * @param reason The new banReason
     */
    public void setBan(long exp, String reason) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> properties = new HashMap<>();
        properties.put("role.banExpiration", exp);
        properties.put("role.banReason", reason);
        String id = getUser().getId();
        rep.update(User.class, id, properties);
        this.banExpiration = exp;
        this.banReason = reason;
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("description",getDescription());
        map.put("banExpiration", getBanExpiration());
        map.put("banReason", getBanReason());
        map.put("totalRatings", this.totalRatings);
        map.put("numRatings", this.numRatings);
        map.put("mealsSold", this.mealsSold);

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {

        Object desc = map.get("description");
        Object exp = map.get("banExpiration");
        Object reason = map.get("banReason");
        Object totalRatings = map.get("totalRatings");
        Object numRatings = map.get("numRatings");
        Object mealsSold = map.get("mealsSold");

        if (desc == null || exp == null || reason == null || totalRatings == null ||
            numRatings == null || mealsSold == null )
            throw new EntityDeserialisationException();

        this.description = (String) desc;
        this.banExpiration = (long) exp;
        this.banReason = (String) reason;
        this.totalRatings = (long) totalRatings;
        this.numRatings = (long) numRatings;
        this.mealsSold = (long) mealsSold;
    }

    /**
     * Helper for debugging
     */
    public void resetRatingsAndMealsSold() throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> properties = new HashMap<>();
        properties.put("role.numRatings", 0);
        properties.put("role.totalRatings", 0);
        properties.put("role.mealsSold", 0);
        String id = getUser().getId();
        rep.update(User.class, id, properties);
        this.numRatings = 0;
        this.totalRatings = 0;
        this.mealsSold = 0;
    }
}
