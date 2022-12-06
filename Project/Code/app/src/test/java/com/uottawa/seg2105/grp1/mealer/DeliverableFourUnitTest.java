package com.uottawa.seg2105.grp1.mealer;

import static org.junit.Assert.*;

import com.uottawa.seg2105.grp1.mealer.model.EntityDeserialisationException;
import com.uottawa.seg2105.grp1.mealer.model.PurchaseRequest;
import com.uottawa.seg2105.grp1.mealer.model.SearchEngine;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DeliverableFourUnitTest {
    @Test
    public void search_checkIsSubsequence() {
        assertTrue("'abd' < 'abcd' should be true", SearchEngine.isSubsequence("abd", "abcd"));
        assertFalse("'abc' < 'abd' should be false", SearchEngine.isSubsequence("abc", "abd"));
        assertTrue("'' < 'abc' should be true", SearchEngine.isSubsequence("", "abc"));
        assertFalse("'abcdef' < 'abcd' should be false", SearchEngine.isSubsequence("abcdef", "abcd"));
        assertFalse("'aaabc' < 'abcdefg' should be false", SearchEngine.isSubsequence("aaabc", "abcdefg"));
    }

    final static Map<String, Object> testPRData = new HashMap<String, Object>() {{
        put("id", "Del. 4 ID");
        put("cookEmail", "cook@email.com");
        put("clientEmail", "client@email.com");
        put("mealId", "Del. 4 ID");
        put("hasBeenRated", false);
        put("hasComplained", false);
        put("status", PurchaseRequest.Status.REJECTED.toString());
    }};

    private PurchaseRequest testPR = new PurchaseRequest();

    @Before
    public void setup() {
        try {
            testPR.deserialise(testPRData);
        } catch (EntityDeserialisationException e) {
            throw new RuntimeException("Invalid PurchaseRequest map");
        }
    }

    @Test
    public void pr_checkGetters() {
        assertEquals(testPR.getId(), testPRData.get("id"));
        assertEquals(testPR.getCookEmail(), testPRData.get("cookEmail"));
        assertEquals(testPR.getClientEmail(), testPRData.get("clientEmail"));
        assertEquals(testPR.getMealId(), testPRData.get("mealId"));
        assertEquals(testPR.getIsRated(), testPRData.get("hasBeenRated"));
        assertEquals(testPR.getIsComplained(), testPRData.get("hasComplained"));
        assertEquals(testPR.getStatus(), PurchaseRequest.Status.valueOf((String)testPRData.get("status")));
    }

    @Test
    public void pr_checkSerialisation() {
        Map<String, Object> serialData = testPR.serialise();

        assertEquals(testPRData, serialData);
    }

    @Test
    public void pr_checkDeserialisation() {
        PurchaseRequest testPR2 = new PurchaseRequest();

        try {
            testPR2.deserialise(testPRData);
        } catch (EntityDeserialisationException e) {
            throw new RuntimeException("Couldn't deserialise purchase request data");
        }

        assertEquals(testPR.compareTo(testPR2), 0);
        try {
            testPR2.rate(5); // Should not fire since the request was rejected
        } catch (Exception e) {
            throw new RuntimeException("Rate should have returned silently");
        }
        try {
            testPR2.complain("Complaint Name", "Complaint Description", testPR2.getClientEmail(), testPR2.getCookEmail()); // Should not fire since the request was rejected
        } catch (Exception e) {
            throw new RuntimeException("Complain should have returned silently");
        }

        assertThrows(EntityDeserialisationException.class, () -> { // Test deserialisation error on no data
            testPR2.deserialise(new HashMap<String, Object>());
        });
    }
}
