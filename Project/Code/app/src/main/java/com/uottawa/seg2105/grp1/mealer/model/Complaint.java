package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
import java.util.Map;

public final class Complaint implements IRepositoryEntity {

    private String id;
    private String title;
    private String description;
    private User client;
    private User cook;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public User getClient() { return client; }
    public User getCook() { return cook; }

    public void create(String title, String description, String clientEmail, String cookEmail) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // Prepare new data
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    Complaint complaint = new Complaint();
                    complaint.id = rep.getAutoID(Complaint.class);
                    complaint.title = title;
                    complaint.description = description;
                    complaint.client = User.getByEmail(clientEmail);
                    complaint.cook = User.getByEmail(cookEmail);

                    //data.put("id", id);
                    //data.put("title", title);
                    //data.put("description", description);
                    //data.put("clientEmail", clientEmail);
                    //data.put("cookEmail", cookEmail);

                    // Create the properties map
                    //Map<String, Object> properties = new HashMap<>();
                    //properties.put("complaint", data);

                    // Update the complaint data
                    rep.set(Complaint.class, complaint);
                    //Complaint.this.id = id;
                    //Complaint.this.title = title;
                    //Complaint.this.description = description;
                    //Complaint.this.client = User.getByEmail(clientEmail);
                    //Complaint.this.cook = User.getByEmail(cookEmail);
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("title",getTitle());
        map.put("description",getDescription());
        map.put("clientEmail",getClient().getEmail());
        map.put("cookEmail",getCook().getEmail());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        Object id = map.get("id");
        Object title = map.get("title");
        Object description = map.get("description");
        Object clientEmail = map.get("clientEmail");
        User client = null;
        if (clientEmail != null) {
            client = User.getByEmail((String) clientEmail);
        }
        Object cookEmail = map.get("cookEmail");
        User cook = null;
        if (cookEmail != null) {
            cook = User.getByEmail((String) cookEmail);
        }

        if (id == null || title == null || description == null || client == null || cook == null) {
            throw new EntityDeserialisationException();
        }
        this.id = (String) id;
        this.title = (String) title;
        this.description = (String) description;
        this.client = client;
        this.cook = cook;
    }
}
