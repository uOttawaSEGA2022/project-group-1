package com.uottawa.seg2105.grp1.mealer.model;

import java.util.Map;

/**
 * Represents an Entity that is serialisable
 */
public interface ISerialisableEntity {

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
}
