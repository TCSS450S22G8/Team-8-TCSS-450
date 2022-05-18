package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.util.Log;

import java.io.Serializable;
/**
 * A class to encapsulate a contact.
 *
 * @author Rin Pham
 * @version 1.0
 */
public class Contact  implements Serializable {
    private final String mUserName;
    private final String mEmail;

    /**
     * Constructor for Contact
     */
    public Contact(String userName, String email) {
        mUserName = userName;
        mEmail = email;
    }

    /**
     * Getter method for User name
     * @return user name
     */
    public String getUserName() {
        return mUserName;
    }


    /**
     * Getter method for Email
     * @return email
     */
    public String getEmail() {
        return mEmail;
    }
}