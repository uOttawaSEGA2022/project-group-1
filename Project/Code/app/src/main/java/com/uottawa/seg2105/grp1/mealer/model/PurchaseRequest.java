package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
import java.util.Map;

public final class PurchaseRequest implements IRepositoryEntity {

    enum Status { PENDING, COMPLETE, REJECTED }

    private String id;
    private User cook;
    private User client;
    private Meal meal;
    private boolean hasBeenRated;
    private boolean hasComplained;
    private Status status;

    public static PurchaseRequest create(User cook, User client, Meal meal) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        PurchaseRequest pr = new PurchaseRequest();
        pr.id = rep.getAutoID(Meal.class);
        pr.cook = cook;
        pr.client = client;
        pr.meal = meal;
        pr.hasBeenRated = false;
        pr.hasComplained = false;
        pr.status = Status.PENDING;
        rep.set(PurchaseRequest.class, pr);
        return pr;
    }

    public User getCook() { return cook; }
    public User getClient() { return client; }

    @Override
    public String getId() {
        return id;
    }

    public void approve(boolean approve) throws RepositoryRequestException {
        String id = this.getId();
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> data = new HashMap<>();
        if (approve) {
            data.put("status", Status.COMPLETE.toString());
            rep.update(PurchaseRequest.class, id, data);
            this.status = Status.COMPLETE;
        } else {
            data.put("status", Status.REJECTED.toString());
            rep.update(PurchaseRequest.class, id, data);
            this.status = Status.REJECTED;
        }
    }

    public void rate(int rating) throws RepositoryRequestException {
        if (!this.hasBeenRated && this.status == Status.COMPLETE) {
            CookRole cookRole = (CookRole) this.cook.getRole();
            cookRole.rate(rating);
            String id = this.getId();
            IRepository rep = MealerSystem.getSystem().getRepository();
            Map<String, Object> data = new HashMap<>();
            data.put("hasBeenRated", true);
            rep.update(PurchaseRequest.class, id, data);
            this.hasBeenRated = true;
        }
    }

    public void complain(String title, String description, String clientEmail, String cookEmail) throws RepositoryRequestException {
        if (!this.hasComplained && this.status == Status.COMPLETE)  {
            Complaint.create(title, description, clientEmail, cookEmail);
            String id = this.getId();
            IRepository rep = MealerSystem.getSystem().getRepository();
            Map<String, Object> data = new HashMap<>();
            data.put("hasComplained", true);
            rep.update(PurchaseRequest.class, id, data);
            this.hasComplained = true;
        }
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("cookEmail", cook.getEmail());
        map.put("clientEmail", client.getEmail());
        map.put("mealId", meal.getId());
        map.put("hasBeenRated", hasBeenRated);
        map.put("hasComplained", hasComplained);
        map.put("status", status.toString());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        Object id = map.get("id");
        Object cookEmail = map.get("cookEmail");
        Object clientEmail = map.get("clientEmail");
        Object mealId = map.get("mealId");
        Object hasBeenRated = map.get("hasBeenRated");
        Object hasComplained = map.get("hasComplained");
        Object status = map.get("status");
        User cook = null;
        User client = null;
        Meal meal = null;
        if (cookEmail != null) {
            cook = User.getByEmail((String) cookEmail);
        }
        if (clientEmail != null) {
            client = User.getByEmail((String) clientEmail);
        }
        if (mealId != null) {
            meal = Meal.getById((String) mealId);
        }

        if (id == null || cook == null || client == null || meal == null || hasBeenRated == null ||
                hasComplained == null || status == null) {
            System.out.print("::::::ID: ");
            System.out.println(id == null);
            System.out.print("::::::COOK: ");
            System.out.println(cook == null);
            System.out.print("::::::CLIENT: ");
            System.out.println(client == null);
            System.out.print("::::::MEAL: ");
            System.out.println(meal == null);
            System.out.print("::::::RATED: ");
            System.out.println(hasBeenRated == null);
            System.out.print("::::::COMPLAINED: ");
            System.out.println(hasComplained == null);
            System.out.print("::::::STATUS: ");
            System.out.println(status == null);
            throw new EntityDeserialisationException();
        }
        this.id = (String) id;
        this.cook = cook;
        this.client = client;
        this.meal = meal;
        this.hasBeenRated = (boolean) hasBeenRated;
        this.hasComplained = (boolean) hasComplained;
        this.status = Enum.valueOf(Status.class, (String) status);
    }
}
