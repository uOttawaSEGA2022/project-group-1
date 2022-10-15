package com.uottawa.seg2105.grp1.mealer.storage;

import com.uottawa.seg2105.grp1.mealer.model.Entity;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * An interface for permanent/persistent storage mechanisms.
 */
public interface IRepository {
    // TODO: Skeleton of IRepository (See classes_explanation.md)
    // TODO: Also create CloudFirestoreRepository

    /**
     * @param cls The Entity subclass to search
     * @param id The id of the element to verify the existence of.
     * @return true if there is such an element in permanent storage, false otherwise
     */
    public <T extends Entity> boolean hasId(Class<T> cls, String id);

    /**
     * Deserialises an entity by fetching it by ID.
     *
     * @param cls The Entity subclass to search
     * @param id The id of the element to fetch.
     * @return The entity that was deserialised, or null if no entity with the specified ID exists
     */
    public <T extends Entity> T getById(Class<T> cls, String id);

    /**
     * Serialises an entity and stores it in persistent storage.
     * This method overwrites any saved element with the same name.
     *
     * NOTE: This method only modifies the storage version of an Entity. Any instance of the Entity must be managed manually.
     *
     * @param cls The Entity subclass to search
     * @param entity The entity of type T that needs to be created in persistent storage
     * @return true if the entity was created or overwrote an element in permanent storage
     */
    public <T extends Entity> boolean set(Class<T> cls, T entity);

    /**
     * Updates the fields of the entity with ID {@code id} with new values specified by the {@code prop} map
     * If no entity with the specified ID exists, this method returns false.
     *
     * NOTE: This method only modifies the storage version of an Entity. Any instance of the Entity must be managed manually.
     *
     * @param cls The Entity subclass to search
     * @param id the id of the Entity to update
     * @param props the properties names and values to update
     * @return true if the update was successful, false otherwise
     */
    public <T extends Entity> boolean update(Class<T> cls, String id, Map<String, Object> props);

    /**
     * Deletes the entity from permanent storage.
     *
     * NOTE: This method only modifies the storage version of an Entity. Any instance of the Entity must be managed manually.
     *
     * @param cls The Entity subclass to search
     * @param entity The entity the delete
     * @return true if the entity was removed or didn't exist yet
     */
    public <T extends Entity> boolean delete(Class<T> cls, T entity);

    /**
     * @return All entities of the specified type in permanent storage
     */
    public <T extends Entity> List<T> list(Class<T> cls);

    /**
     * Queries permanent storage for all entities matching the condition specified by the predicate.
     *
     * @param cls The Entity subclass to search
     * @param predicate The predicate representing the condition of the query
     * @return All entities who match the predicate
     */
    public <T extends Entity> List<T> query(Class<T> cls, Predicate<T> predicate);
}
