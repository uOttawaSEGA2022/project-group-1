package com.uottawa.seg2105.grp1.mealer;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import android.util.Pair;

import com.uottawa.seg2105.grp1.mealer.lib.Utility;

import java.util.List;

/**
 * Tests the Utility Class's isValidField
 */
public class DeliverableOneUnitTest {

    /**
     * Test 1
     * Try a variety of tests for the NAME regex
     */
    @Test
    public void utility_ValidateName() {
        // Test the NAME format
        assertTrue("Single Letter Name",
                Utility.isValidField("A", Utility.NAME));
        assertTrue("Single Name",
                Utility.isValidField("John", Utility.NAME));
        assertTrue("Composite Name",
                Utility.isValidField("Se-pa'ra ted", Utility.NAME));
        assertFalse("Empty Name",
                Utility.isValidField("", Utility.NAME));
        assertFalse("Initial Non-Letter Name",
                Utility.isValidField("0ab", Utility.NAME));
        assertFalse("Initial Non-Capital Name",
                Utility.isValidField("abc", Utility.NAME));
        assertFalse("Non-Letter Name",
                Utility.isValidField("Ab0c", Utility.NAME));
        assertFalse("Invalid Separator Name",
                Utility.isValidField("John_Doe", Utility.NAME));

    }

    /**
     * Test 2
     * Try a variety of tests for the EMAIL regex
     */
    @Test
    public void utility_ValidateEmail() {
        // Test the EMAIL format
        assertTrue("Simple Email",
                Utility.isValidField("email@email.com", Utility.EMAIL));
        assertTrue("Complex Email",
                Utility.isValidField("com-plex.email@email.com", Utility.EMAIL));
        assertTrue("Two letter top-level domain Email",
                Utility.isValidField("email@email.co", Utility.EMAIL));
        assertFalse("Empty Email",
                Utility.isValidField("", Utility.EMAIL));
        assertFalse("Local part too long Email",
                Utility.isValidField("12345678901234567890123456789012345678901234567890123456789012345@test.com", Utility.EMAIL));
        assertFalse("Empty local part Email",
                Utility.isValidField("@email.com", Utility.EMAIL));
        assertFalse("Empty domain Email",
                Utility.isValidField("email@", Utility.EMAIL));
        assertFalse("Invalid Email",
                Utility.isValidField(".email.@email.com", Utility.EMAIL));
        assertFalse("No domain name Email",
                Utility.isValidField("email@.com", Utility.EMAIL));
        assertFalse("Invalid top-level domain Email",
                Utility.isValidField("email@email.a", Utility.EMAIL));
        assertFalse("Multiple @ Email",
                Utility.isValidField("a@b@email.com", Utility.EMAIL));

    }

    /**
     * Test 3
     * Try a variety of tests for the ADDRESS regex
     */
    @Test
    public void utility_ValidateAddress() {
        // Test the ADDRESS format
        assertTrue("Valid Address",
                Utility.isValidField("800 King Edward Ave, Ottawa, ON K1N 6N5", Utility.ADDRESS));
        assertFalse("Empty Address",
                Utility.isValidField("", Utility.ADDRESS));
        assertFalse("Invalid Address",
                Utility.isValidField("$InvalidAddress", Utility.ADDRESS));
    }

    /**
     * Test 4
     * Try a variety of tests for the CREDITCARD and CREDITCARDEXPIRY regex
     */
    @Test
    public void utility_ValidateCreditCard() {
        // Test the CREDITCARD format
        assertTrue("16 numbers Credit Card",
                Utility.isValidField("1234567890123456", Utility.CREDITCARD));
        assertFalse("Non-numeric Credit Card",
                Utility.isValidField("A234567890123456", Utility.CREDITCARD));
        assertFalse("Less than 16 numbers Credit Card",
                Utility.isValidField("123456789012345", Utility.CREDITCARD));
        assertFalse("More than 16 numbers Credit Card",
                Utility.isValidField("12345678901234567", Utility.CREDITCARD));

        // Test the CREDITCARDEXPIRY format
        assertTrue("Valid pre-November Expiration",
                Utility.isValidField("01/01", Utility.CREDITCARDEXPIRY));
        assertTrue("Valid post-October Expiration",
                Utility.isValidField("11/30", Utility.CREDITCARDEXPIRY));
        assertFalse("Invalid pre-October Expiration",
                Utility.isValidField("1/01", Utility.CREDITCARDEXPIRY));
        assertFalse("Day too short Expiration",
                Utility.isValidField("01/1", Utility.CREDITCARDEXPIRY));
        assertFalse("Day too long Expiration",
                Utility.isValidField("01/001", Utility.CREDITCARDEXPIRY));
        assertFalse("Month too short Expiration",
                Utility.isValidField("1/01", Utility.CREDITCARDEXPIRY));
        assertFalse("Month too long Expiration",
                Utility.isValidField("001/01", Utility.CREDITCARDEXPIRY));
        assertFalse("Month 0 Expiration",
                Utility.isValidField("00/01", Utility.CREDITCARDEXPIRY));
        assertFalse("Month 13 Expiration",
                Utility.isValidField("13/01", Utility.CREDITCARDEXPIRY));
    }
}
