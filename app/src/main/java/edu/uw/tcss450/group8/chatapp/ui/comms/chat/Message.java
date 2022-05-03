package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import java.io.Serializable;

/**
 * A class to encapsulate a chat message.
 *
 * @author jliaoh
 * @version 1.0
 */
public class Message implements Serializable {
    private final String mMessage;
    private final String mSent;
    private final String mSentDate;

    /**
     * Constructor for Message
     * @param message chat message
     * @param sent who sent the message
     * @param sentDate the time message is sent
     */
    public Message(String message, String sent, String sentDate) {
        mMessage = message;
        mSent = sent;
        mSentDate = sentDate;
    }

    /**
     * getter for message
     * @return message
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * getter for sent
     * @return sent
     */
    public String getSent() {
        return mSent;
    }

    /**
     * getter for date
     * @return
     */
    public String getSentDate() {
        return mSentDate;
    }
}
