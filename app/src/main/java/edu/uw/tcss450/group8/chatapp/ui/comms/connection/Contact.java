package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import java.io.Serializable;
    /**
     * A class to encapsulate a contact.
     *
     * @author Rin Pham
     * @version 1.0
     */
    public class Contact  implements Serializable {
        private final String mUserName;
        private final String mFirstName;
        private final String mLastName;
        private final String mEmail;

        /**
         * Constructor for Contact
         */
        public Contact(String userName, String firstName, String lastName, String email) {
            mUserName = userName;
            mFirstName = firstName;
            mLastName = lastName;
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
         * Getter method for First name
         * @return first name
         */
        public String getFirstName() {
            return mFirstName;
        }

        /**
         * Getter method for Last name
         * @return last name
         */
        public String getLastName() {
            return mLastName;
        }

        /**
         * Getter method for Email
         * @return email
         */
        public String getEmail() {
            return mEmail;
        }
    }


