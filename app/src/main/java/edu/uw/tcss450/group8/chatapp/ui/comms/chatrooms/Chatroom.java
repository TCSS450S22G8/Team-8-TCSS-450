package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import java.io.Serializable;


/**
 * A class to encapsulate a chat message.
 *
 * @author Levi McCoy
 * @version 1.0
 */
public class Chatroom implements Serializable {
    //    private final String mMessage;
//    private final String mSent;
    private final String mChatRoomName;
    private final String mChatRoomId;

    /**
     * Constructor for Message
     *
     * @param chatRoomId  id of chat room
     * @param chatRoomName the name of the chat
     */
    public Chatroom(String chatRoomId, String chatRoomName) {
        //mMessage = message; Will add these
        //mSent = sent;
        mChatRoomId = chatRoomId;
        mChatRoomName = chatRoomName;
    }

//    /**
//     * Getter for message
//     *
//     * @return message string(s)
//     */
//    public String getMessage() {
//        return mMessage;
//    }

//    /**
//     * Getter for sent
//     *
//     * @return sent message
//     */
//    public String getSent() {
//        return mSent;
//    }

    /**
     * Getter for date
     *
     * @return returns date of sent message
     */
    public String getChatRoomName() {
        return mChatRoomName;
    }

    public String getChatRoomId() {
        return mChatRoomId;
    }
}