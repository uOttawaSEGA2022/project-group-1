package com.uottawa.seg2105.grp1.mealer.model;

/**
 * Represents the generic UserRole of a User Entity
 */
public abstract class UserRole implements ISerialisableEntity {
    /**
     * The User this UserRole belongs to
     */
    User user;

    /**
     *
     * @return the User Entity of this UserRole
     */
    public final User getUser() {
        return user;
    }
}
