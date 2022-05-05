package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create Dummy Message. Use it for development.
 *
 * @author jliaoh
 * @version 1.0
 */
public final class MessageGenerator {

    private static final Message[] MESSAGES;
    public static final int COUNT = 20;

    /**
     *  generate message
     */
    static {
        MESSAGES = new Message[COUNT];
        for (int i = 0; i < MESSAGES.length; i++) {
            MESSAGES[i] = new Message("Message "+i, "John", "2020-04-" + (i + 1) + " 12:59 pm");
        }
    }

    /**
     * get list of generated message
     * @return list of message
     */
    public static List<Message> getMessageList() {
        return Arrays.asList(MESSAGES);
    }
}
