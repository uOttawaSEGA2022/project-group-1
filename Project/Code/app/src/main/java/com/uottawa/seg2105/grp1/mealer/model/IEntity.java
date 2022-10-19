package com.uottawa.seg2105.grp1.mealer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Entity that can be stored in and retrieved from permanent storage.
 */
public interface IEntity {
    /**
     * A map of Entity subclasses to their table names, for use by Repository
     * All subclasses of Entity should register a name here
     *
     * For example: {@code static { Entity.tableNames.put(CLASS_NAME_HERE.class, "TABLE_NAME_HERE"); } }
     */
    Map<Class<? extends IEntity>, String> tableNames = new HashMap<>();

    /**
     * Returns an ID uniquely identifying this Entity within its table.
     * @return the ID of this Entity
     */
    String getId();

    /**
     * Returns a JSON-compatible Map of this Entity whose format is decided by the subclass.
     * @return A JSON-compatible Map description of the object.
     */
    Map<String, Object> serialise();

    /**
     * Overwrites the properties of an Entity using the provided Map.
     *
     * @param map The map of properties whose format is decided by the subclass
     *
     * @exception EntityDeserialisationException if the Map was invalid
     */
    void deserialise(Map<String, Object> map) throws EntityDeserialisationException;

    /**
     * Returns the table name associate to the specified Entity subclass, or null if no table name has been specified.
     * @param cls The class whose table name is being queried
     * @return The table name associated with the specified class
     */
    static String getTableName(Class<? extends IEntity> cls) {
        return tableNames.get(cls);
    }

}
