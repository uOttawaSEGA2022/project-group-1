package com.uottawa.seg2105.grp1.mealer.model;

/**
 * Represents the generic UserRole of a User Entity
 */
public abstract class UserRole extends EntityFragment {
    /**
     * The User this UserRole belongs to
     */
    private User user;

    /**
     *
     * @return the User Entity of this UserRole
     */
    public final User getUser() {
        return user;
    }
}
