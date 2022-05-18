
package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import java.util.Arrays;
import java.util.List;


/**
 * This class is used to create Dummy Chatrooms. Use it for development.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @version 1.0
 */
public final class ChatroomGenerator {

    private static final Chatroom[] CHATROOMS;
    public static final int COUNT = 4;

    /**
     *  generate mock chatroom
     */
    static {
        CHATROOMS = new Chatroom[COUNT];
        String sender = "John";
        for (int i = 0; i < CHATROOMS.length; i++) {
            if(i==1){
                sender = "Carl";
            }
            CHATROOMS[i] = new Chatroom("Message " + i, sender, "2020-04-" + (i + 1) + " 12:59 pm");
        }
    }

    /**
     * Get list of generated message
     *
     * @return list of message
     */
    public static List<Chatroom> getChatroomList() {
        return Arrays.asList(CHATROOMS);
    }
}
