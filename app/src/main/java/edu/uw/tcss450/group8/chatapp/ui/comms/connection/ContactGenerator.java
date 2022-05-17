package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create Dummy Contact. Use it for development.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @version 1.0
 */
public final class ContactGenerator {

    private static final Contact[] CONTACTS;
    public static final int COUNT = 20;

    /**
     *  generate contact
     */
    static {
        CONTACTS = new Contact[COUNT];
        for (int i = 0; i < CONTACTS.length; i++) {
            CONTACTS[i] = new Contact("testUser " + i, "test1@test.com");
        }
    }

    /**
     * Get list of generated contact
     *
     * @return list of contact
     */
    public static List<Contact> getContactList() {
        return Arrays.asList(CONTACTS);
    }
}
