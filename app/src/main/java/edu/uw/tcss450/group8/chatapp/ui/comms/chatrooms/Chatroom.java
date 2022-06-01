package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import java.io.Serializable;


/**
 * A class to encapsulate a chat message object.
 *
 * @author Levi McCoy
 * @version 5/19/22
 */
public class Chatroom implements Serializable {
    private final String mChatRoomName;
    private final String mChatRoomId;
    private final String mOwner;

    /**
     * Constructor for Message
     *
     * @param chatRoomId  id of chat room
     * @param chatRoomName the name of the chat
     */
    public Chatroom(String chatRoomId, String chatRoomName, String owner) {
        mChatRoomId = chatRoomId;
        mChatRoomName = chatRoomName;
        mOwner = owner;
    }


    /**
     * Getter for chat room nickname
     *
     * @return returns nickname of the chat room
     */
    public String getChatRoomName() {
        return mChatRoomName;
    }

    /**
     * Getter for chat room id
     *
     * @return returns the id number of the chat room
     */
    public String getChatRoomId() {
        return mChatRoomId;
    }

    /**
     * Getter for room owner
     *
     * @return returns the email of the room owner
     */
    public String getChatOwner() {
        return mOwner;
    }
}