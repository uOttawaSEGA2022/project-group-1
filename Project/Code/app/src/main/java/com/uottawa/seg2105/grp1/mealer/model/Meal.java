package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
import java.util.Map;

public final class Meal implements IRepositoryEntity {

    public static final String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Other"};
    public static final String[] cuisineTypes = {"Italian", "Thai", "Indian", "Other"};

    private String id;
    private String name;
    private String type;
    private String cuisine;
    private String ingredients;
    private String allergens;
    private int price;
    private String description;

    private User cook;
    private boolean isOffered;

    public String getName() { return name; }
    public String getType() { return type; }
    public String getCuisine() { return cuisine; }
    public String getIngredients() { return ingredients; }
    public String getAllergens() { return allergens; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public User getCook() { return cook; }
    public boolean getIsOffered() { return isOffered; }


    public void flipIsOffered() throws InterruptedException {
        String id = this.getId();
        boolean newVal = !this.isOffered;
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    // Prepare new data
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    Map<String, Object> data = new HashMap<>();
                    data.put("isOffered", newVal);

                    rep.update(Meal.class, id, data);

                    Meal.this.isOffered = newVal;
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }


    public static Meal createMeal(String name, String type, String cuisine,
                                  String ingredients, String allergens, int price, String description,
                                  User cook, boolean isOffered) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        Meal meal = new Meal();
        meal.id = rep.getAutoID(Meal.class);
        meal.name = name;
        meal.type = type;
        meal.cuisine = cuisine;
        meal.ingredients = ingredients;
        meal.allergens = allergens;
        meal.price = price;
        meal.description = description;
        meal.cook = cook;
        meal.isOffered = isOffered;
        rep.set(Meal.class, meal);
        return meal;
    }

    public Meal updateMeal(String name, String type, String cuisine,
                           String ingredients, String allergens, int price, String description,
                           User cook, boolean isOffered) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        rep.delete(Meal.class, this);
        Meal meal = Meal.createMeal(name, type, cuisine, ingredients, allergens, price, description, cook, isOffered);
        return meal;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("name",getName());
        map.put("type",getType());
        map.put("cuisine",getCuisine());
        map.put("ingredients",getIngredients());
        map.put("allergens",getAllergens());
        map.put("price",getPrice());
        map.put("description",getDescription());
        map.put("cookEmail",getCook().getEmail());
        map.put("isOffered", getIsOffered());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {
        Object id = map.get("id");
        Object name = map.get("name");
        Object type = map.get("type");
        Object cuisine = map.get("cuisine");
        Object ingredients = map.get("ingredients");
        Object allergens = map.get("allergens");
        Object price = map.get("price");
        Object description = map.get("description");
        Object cookEmail = map.get("cookEmail");
        Object isOffered = map.get("isOffered");
        User cook = null;
        if (cookEmail != null) {
            cook = User.getByEmail((String) cookEmail);
        }

        if (id == null || name == null || type == null || cuisine == null || ingredients == null ||
                allergens == null || price == null || description == null ||
                cook == null || isOffered == null) {
            throw new EntityDeserialisationException();
        }
        this.id = (String) id;
        this.name = (String) name;
        this.type = (String) type;
        this.cuisine = (String) cuisine;
        this.ingredients = (String) ingredients;
        this.allergens = (String) allergens;
        this.price = ((Long) price).intValue();
        this.description = (String) description;
        this.cook = cook;
        this.isOffered = (boolean) isOffered;
    }
}