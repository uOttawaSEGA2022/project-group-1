package com.uottawa.seg2105.grp1.mealer.model;

import java.util.Map;

/**
 * Stores extra information about Client Users.
 */
public class ClientRole extends UserRole {
    /**
     * The credit card number of the client
     */
    private String cardNumber;

    /**
     * @return The credit card number of this client.
     */
    public String getCardNumber() { return cardNumber; }

    /**
     * Updates the client's credit card number.
     * @param cardNumber The new credit card number.
     */
    public void setCardNumber(String cardNumber) {
        // TODO: Call repository to update in storage
        this.cardNumber = cardNumber;
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Map<String, Object> serialise() {
        // TODO: implement ClientRole.serialise();
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean deserialise(Map<String, Object> map) {
        // TODO: implement ClientRole.deserialise()
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
