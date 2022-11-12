package com.uottawa.seg2105.grp1.mealer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.uottawa.seg2105.grp1.mealer.model.ClientRole;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.EntityDeserialisationException;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.UserRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests User's serialisation and deserialisation, as well as its getters and its role
 */
public class DeliverableThreeUnitTest {
    final static Map<String, Object> testClientRoleData = new HashMap<String, Object>() {{
        put("cardNumber", "1234567890123456");
    }};

    final static Map<String, Object> testCookRoleData = new HashMap<String, Object>() {{
        put("banExpiration", -1l);
        put("banReason", "");
        put("description", "test description");
        put("mealsSold", 0l);
        put("numRatings", 0l);
        put("totalRatings", 0l);
    }};

    final static Map<String, Object> testCookData = new HashMap<String, Object>() {{
        put("address", "testaddress");
        put("admin", false);
        put("email", "test@email.com");
        put("firstName", "Testfn");
        put("lastName", "Testln");
        put("passwordHash", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3"); // Password: 123
        put("roleType", "Cook");
        put("role", testCookRoleData);
    }};

    private User testCook = new User();

    @Before
    public void setup() {
        try {
            testCook.deserialise(testCookData);
        } catch (EntityDeserialisationException e) {
            throw new RuntimeException("Invalid user map");
        }
    }

    @Test
    public void user_CheckGetters() {
        assertEquals(testCook.getAddress(), testCookData.get("address"));
        assertFalse(testCook.isAdmin());
        assertEquals(testCook.getEmail(), testCookData.get("email"));

        CookRole role = (CookRole) testCook.getRole();
        Map<String, Object> roleData = (Map<String, Object>) testCookData.get("role");

        assertEquals(role.getBanExpiration(), roleData.get("banExpiration"));
        assertEquals(role.getBanReason(), roleData.get("banReason"));
        assertEquals(role.getDescription(), roleData.get("description"));
    }

    @Test
    public void user_checkSerialisation() {
        Map<String, Object> serialData = testCook.serialise();

        assertEquals(testCookData, serialData);
    }

    @Test
    public void user_checkDeserialisation() {
        User testCook2 = new User();

        try {
            testCook2.deserialise(testCook.serialise());

            assertEquals(testCook.getId(), testCook2.getId());
            assertEquals(testCook.getAddress(), testCook2.getAddress());
            assertEquals(testCook.isAdmin(), testCook2.isAdmin());
            assertEquals(testCook.getEmail(), testCook2.getEmail());
            assertEquals(testCook.getFirstName(), testCook2.getFirstName());
            assertEquals(testCook.getLastName(), testCook2.getLastName());
        } catch (EntityDeserialisationException e) {
            throw new RuntimeException("Invalid deserialisation.");
        }
    }

    @Test
    public void role_checkFormats() {
        Map<String, Object> cookRoleData = new HashMap<String, Object>((Map<String, Object>) testCookData.get("role"));

        UserRole clientRole = new ClientRole();
        UserRole cookRole = new CookRole();

        try {
            clientRole.deserialise(testClientRoleData);
        } catch (EntityDeserialisationException e) {
            throw new RuntimeException("Couldn't deserialise client role data");
        }
        try {
            cookRole.deserialise(testCookRoleData);
        } catch (EntityDeserialisationException e) {
            throw new RuntimeException("Couldn't deserialise cook role data");
        }

        assertTrue(clientRole instanceof ClientRole);
        assertTrue(cookRole instanceof CookRole);
        assertFalse(clientRole instanceof CookRole);
        assertFalse(cookRole instanceof ClientRole);

        // Test deserialisation error
        assertThrows(EntityDeserialisationException.class, () -> {
            clientRole.deserialise(testCookRoleData);
        });
        assertThrows(EntityDeserialisationException.class, () -> {
            cookRole.deserialise(testClientRoleData);
        });
    }
}
