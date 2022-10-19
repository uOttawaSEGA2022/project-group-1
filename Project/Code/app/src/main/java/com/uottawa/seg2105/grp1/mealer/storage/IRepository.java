package com.uottawa.seg2105.grp1.mealer.storage;

import com.uottawa.seg2105.grp1.mealer.model.IEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * An interface for permanent/persistent storage mechanisms.
 */
public interface IRepository {
    /**
     * @param cls The Entity subclass to search
     * @param id The id of the element to verify the existence of.
     * @return true if there is such an element in permanent storage, false otherwise
     *
     * @exception RepositoryRequestException if data could not be fetched
     */
    <T extends IEntity> boolean hasId(Class<T> cls, String id) throws RepositoryRequestException;

    /**
     * Deserialises an entity by fetching it by ID.
     *
     * @param cls The Entity subclass to search
     * @param id The id of the element to fetch.
     * @return The entity that was deserialised, or null if no entity with the specified ID exists
     *
     * @exception RepositoryRequestException if data could not be fetched
     */
    <T extends IEntity> T getById(Class<T> cls, String id) throws RepositoryRequestException;

    /**
     * Serialises an entity and stores it in persistent storage.
     * This method overwrites any saved element with the same name.
     *
     * NOTE: This method only modifies the storage version of an Entity. Any instance of the Entity must be managed manually.
     *
     * @param cls The Entity subclass to search
     * @param entity The entity of type T that needs to be created in persistent storage
     *
     * @exception RepositoryRequestException if data could not be added or overwritten
     */
    <T extends IEntity> void set(Class<T> cls, T entity) throws RepositoryRequestException;

    /**
     * Updates the fields of the entity with ID {@code id} with new values specified by the {@code prop} map
     * Does nothing if there was no entity to update in permanent storage.
     *
     * NOTE: This method only modifies the storage version of an Entity. Any instance of the Entity must be managed manually.
     *
     * @param cls The Entity subclass to search
     * @param id the id of the Entity to update
     * @param props the properties names and values to update
     *
     * @exception RepositoryRequestException if data could not be accessed
     */
    <T extends IEntity> void update(Class<T> cls, String id, Map<String, Object> props) throws RepositoryRequestException;

    /**
     * Deletes the entity from permanent storage.
     *
     * NOTE: This method only modifies the storage version of an Entity. Any instance of the Entity must be managed manually.
     *
     * @param cls The Entity subclass to search
     * @param entity The entity the delete
     *
     * @exception RepositoryRequestException if data could not be deleted
     */
    <T extends IEntity> void delete(Class<T> cls, T entity) throws RepositoryRequestException;

    /**
     * @return All entities of the specified type in permanent storage
     *
     * @exception RepositoryRequestException if data could not be fetched
     */
    <T extends IEntity> List<T> list(Class<T> cls) throws RepositoryRequestException;

    /**
     * Queries permanent storage for all entities matching the condition specified by the predicate.
     *
     * @param cls The Entity subclass to search
     * @param predicate The predicate representing the condition of the query
     * @return All entities who match the predicate
     *
     * @exception RepositoryRequestException if data could not be fetched
     */
    <T extends IEntity> List<T> query(Class<T> cls, Predicate<T> predicate) throws RepositoryRequestException;
}
