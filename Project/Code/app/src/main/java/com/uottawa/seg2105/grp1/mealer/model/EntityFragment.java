package com.uottawa.seg2105.grp1.mealer.model;

/**
 * Represents an Entity that is stored within another Entity.
 * As such, its getId method returns null.
 */
public abstract class EntityFragment implements IEntity {
    /**
     * @return Always null
     */
    public final String getId() { return null; }
}
