package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Complaint implements IRepositoryEntity {

    private String id;
    private String title;
    private String description;
    private User client;
    private User cook;
    private boolean isArchived;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public User getClient() { return client; }
    public User getCook() { return cook; }
    public boolean isArchived() { return isArchived; }

    public static Complaint create(String title, String description, String clientEmail, String cookEmail) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Complaint complaint = new Complaint();
        complaint.id = rep.getAutoID(Complaint.class);
        complaint.title = title;
        complaint.description = description;
        complaint.client = User.getByEmail(clientEmail);
        complaint.cook = User.getByEmail(cookEmail);
        complaint.isArchived = false;
        rep.set(Complaint.class, complaint);
        return complaint;
    }

    public void archive() throws InterruptedException {
        String id = this.getId();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    // Prepare new data
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    Map<String, Object> data = new HashMap<>();
                    data.put("isArchived", true);

                    rep.update(Complaint.class, id, data);

                    Complaint.this.isArchived = true;
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    /**
     * Utility function to help with testing
     * @throws RepositoryRequestException
     */
    public static void unarchiveAll() throws RepositoryRequestException {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    // Prepare new data
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    List<Complaint> complaintList= rep.list(Complaint.class);
                    for (Complaint c : complaintList) {
                        String id = c.getId();
                        Map<String, Object> data = new HashMap<>();
                        data.put("isArchived", false);
                        rep.update(Complaint.class, id, data);
                    }
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
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
        map.put("isArchived", isArchived());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        Object id = map.get("id");
        Object title = map.get("title");
        Object description = map.get("description");
        Object clientEmail = map.get("clientEmail");
        Object isArchived = map.get("isArchived");
        User client = null;
        if (clientEmail != null) {
            client = User.getByEmail((String) clientEmail);
        }
        Object cookEmail = map.get("cookEmail");
        User cook = null;
        if (cookEmail != null) {
            cook = User.getByEmail((String) cookEmail);
        }

        if (id == null || title == null || description == null ||
                client == null || cook == null || isArchived == null) {
            throw new EntityDeserialisationException();
        }
        this.id = (String) id;
        this.title = (String) title;
        this.description = (String) description;
        this.client = client;
        this.cook = cook;
        this.isArchived = (boolean) isArchived;
    }
}