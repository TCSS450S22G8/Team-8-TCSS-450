package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import java.io.Serializable;


/**
 * A class to encapsulate a contact.
 *
 * @author Rin Pham
 * @author Shilnara Dam
 * @version 5/19/22
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