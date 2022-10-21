package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
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
        this.cardNumber = cardNumber;
        String email = this.user.getEmail();
        new Thread() {
            @Override
            public void run() {
                try {
                    // Create or overwrite the User in the repository.
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    Map<String, Object> data = new HashMap<>();
                    data.put("cardNumber", cardNumber);
                    rep.update(User.class, email, data);
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public Map<String, Object> serialise() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardNumber",getCardNumber());

        return map;
    }

    @Override
    public void deserialise(Map<String, Object> map) throws EntityDeserialisationException {

        Object cardNum = map.get("cardNumber");

        if (cardNum == null) {
            throw new EntityDeserialisationException();
        }
        this.cardNumber = (String) cardNum;
    }
}
