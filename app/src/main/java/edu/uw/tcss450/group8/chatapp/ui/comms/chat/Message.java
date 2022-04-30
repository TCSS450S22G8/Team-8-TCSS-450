package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import java.io.Serializable;

/**
 * A class to encapsulate a chat message.
 *
 * @author jliaoh
 * @version 4/27/2022
 */
public class Message implements Serializable {
    private final String mMessage;
    private final String mSent;
    private final String mSentDate;

    /**
     * Constructor of Message
     * @param message chat message
     * @param sent who sent the message
     * @param sentDate the time message is sent
     */
    public Message(String message, String sent, String sentDate) {
        mMessage = message;
        mSent = sent;
        mSentDate = sentDate;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSent() {
        return mSent;
    }

    public String getSentDate() {
        return mSentDate;
    }
}
