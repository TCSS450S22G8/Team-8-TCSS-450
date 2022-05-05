package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import java.io.Serializable;


/**
 * A class to encapsulate a chat message.
 *
 * @author JenHo Liao
 * @version 1.0
 */
public class Message implements Serializable {
    private final String mMessage;
    private final String mSent;
    private final String mSentDate;

    /**
     * Constructor for Message
     *
     * @param message  chat message
     * @param sent     who sent the message
     * @param sentDate the time message is sent
     */
    public Message(String message, String sent, String sentDate) {
        mMessage = message;
        mSent = sent;
        mSentDate = sentDate;
    }

    /**
     * Getter for message
     *
     * @return message string(s)
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Getter for sent
     *
     * @return sent message
     */
    public String getSent() {
        return mSent;
    }

    /**
     * Getter for date
     *
     * @return returns date of sent message
     */
    public String getSentDate() {
        return mSentDate;
    }
}
