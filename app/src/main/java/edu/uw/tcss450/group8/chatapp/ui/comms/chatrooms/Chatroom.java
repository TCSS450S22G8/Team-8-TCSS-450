package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import java.io.Serializable;


/**
 * A class to encapsulate a chat message.
 *
 * @author Levi McCoy
 * @version 1.0
 */
public class Chatroom implements Serializable {
    private final String mMessage;
    private final String mSent;
    private final String mSentDate;
    private final int mChatRoomId;

    /**
     * Constructor for Message
     *
     * @param message  latest chat message
     * @param sent     who sent the message
     * @param sentDate the time message is sent
     */
    public Chatroom(String message, String sent, String sentDate) {
        mMessage = message;
        mSent = sent;
        mSentDate = sentDate;
        mChatRoomId = 1;
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

    public int getChatRoomId() {
        return mChatRoomId;
    }
}
