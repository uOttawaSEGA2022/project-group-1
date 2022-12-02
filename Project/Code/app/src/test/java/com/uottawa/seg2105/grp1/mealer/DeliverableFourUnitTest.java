package com.uottawa.seg2105.grp1.mealer;

import static org.junit.Assert.*;

import com.uottawa.seg2105.grp1.mealer.model.SearchEngine;

import org.junit.Test;

public class DeliverableFourUnitTest {
    @Test
    public void search_checkIsSubsequence() {
        assertTrue("'abd' < 'abcd' should be true", SearchEngine.isSubsequence("abd", "abcd"));
        assertFalse("'abc' < 'abd' should be false", SearchEngine.isSubsequence("abc", "abd"));
        assertTrue("'' < 'abc' should be true", SearchEngine.isSubsequence("", "abc"));
        assertFalse("'abcdef' < 'abcd' should be false", SearchEngine.isSubsequence("abcdef", "abcd"));
        assertFalse("'aaabc' < 'abcdefg' should be false", SearchEngine.isSubsequence("aaabc", "abcdefg"));
    }
}
