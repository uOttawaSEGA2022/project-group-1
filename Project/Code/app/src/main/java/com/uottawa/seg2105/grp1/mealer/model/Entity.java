package com.uottawa.seg2105.grp1.mealer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Entity that can be stored in and retrieved from permanent storage.
 */
public abstract class Entity {
    /**
     * A map of Entity subclasses to their table names, for use by Repository (Should we move this to IRepository instead?)
     */
    static final Map<Class<? extends Entity>, String> tableNames = new HashMap<>();
    static {
        // TODO: Initialise this Map with all table names in the Firestore database
    }

    /**
     * Returns an ID uniquely identifying this Entity within its table.
     * @return the ID of this Entity
     */
    public abstract String getId();

    /**
     * Returns a JSON-compatible Map of this Entity whose format is decided by the subclass.
     * @return A JSON-compatible Map description of the object.
     */
    public abstract Map<String, Object> serialise();

    /**
     * Overwrites the properties of an Entity using the provided Map and returns true.
     * If the Map is of an incorrect format, return false.
     * @param map The map of properties whose format is decided by the subclass
     * @return Whether deserialisation was performed or not
     */
    public abstract boolean deserialise(Map<String, Object> map);

}
