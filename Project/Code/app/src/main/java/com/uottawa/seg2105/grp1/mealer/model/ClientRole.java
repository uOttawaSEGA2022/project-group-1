package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    public void setCardNumber(String cardNumber) throws RepositoryRequestException {
        // Prepare new data
        IRepository rep = MealerSystem.getSystem().getRepository();
        Map<String, Object> data = new HashMap<>();
        data.put("cardNumber", cardNumber);

        // Create the properties map
        Map<String, Object> properties = new HashMap<>();
        properties.put("role", data);

        // Update the role data
        String id = getUser().getId();
        rep.update(User.class, id, properties);

        ClientRole.this.cardNumber = cardNumber;
    }

    public List<PurchaseRequest> getPurchaseRequests() throws RepositoryRequestException {
        List<PurchaseRequest> purchases;
        String email = this.getUser().getEmail();
        IRepository rep = MealerSystem.getSystem().getRepository();
        purchases = rep.query(PurchaseRequest.class,
                (pr) -> pr.getClientEmail().equals(email)
        );
        Collections.sort(purchases);
        return purchases;
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
