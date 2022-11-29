package com.uottawa.seg2105.grp1.mealer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Entity that can be stored in and retrieved from permanent storage by means of a unique ID.
 */
public interface IRepositoryEntity extends ISerialisableEntity {
    /**
     * A map of Entity subclasses to their table names, for use by Repository
     * All subclasses of Entity should register a name here
     *
     * For example: {@code static { Entity.tableNames.put(CLASS_NAME_HERE.class, "TABLE_NAME_HERE"); } }
     */
    Map<Class<? extends IRepositoryEntity>, String> tableNames = new HashMap<Class<? extends IRepositoryEntity>, String>() {
        {
            put(User.class, "users");
            put(Complaint.class, "complaints");
            put(Meal.class, "meals");
            put(PurchaseRequest.class, "purchases");
        }
    };

    /**
     * Returns an ID uniquely identifying this Entity within its table.
     * @return the ID of this Entity
     */
    String getId();

    /**
     * Returns the table name associate to the specified Entity subclass, or null if no table name has been specified.
     * @param cls The class whose table name is being queried
     * @return The table name associated with the specified class
     */
    static String getTableName(Class<? extends IRepositoryEntity> cls) {
        return tableNames.get(cls);
    }

}
