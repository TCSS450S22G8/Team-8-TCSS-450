package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Encapsulate chat message details.
 */
public final class Message implements Serializable {

    private final int mMessageId;
    private final String mMessage;
    private final String mSender;
    private final String mTimeStamp;

    public Message(int messageId, String message, String sender, String timeStamp) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static Message createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new Message(msg.getInt("messageid"),
                msg.getString("message"),
                msg.getString("email"),
                msg.getString("timestamp"));
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getMessageId() {
        return mMessageId;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof Message) {
            result = mMessageId == ((Message) other).mMessageId;
        }
        return result;
    }
}



//package edu.uw.tcss450.group8.chatapp.ui.comms.chat;
//
//import java.io.Serializable;
//
//
///**
// * A class to encapsulate a chat message.
// *
// * @author JenHo Liao
// * @version 1.0
// */
//public class Message implements Serializable {
//    private final String mMessage;
//    private final String mSent;
//    private final String mSentDate;
//
//    /**
//     * Constructor for Message
//     *
//     * @param message  chat message
//     * @param sent     who sent the message
//     * @param sentDate the time message is sent
//     */
//    public Message(String message, String sent, String sentDate) {
//        mMessage = message;
//        mSent = sent;
//        mSentDate = sentDate;
//    }
//
//    /**
//     * Getter for message
//     *
//     * @return message string(s)
//     */
//    public String getMessage() {
//        return mMessage;
//    }
//
//    /**
//     * Getter for sent
//     *
//     * @return sent message
//     */
//    public String getSent() {
//        return mSent;
//    }
//
//    /**
//     * Getter for date
//     *
//     * @return returns date of sent message
//     */
//    public String getSentDate() {
//        return mSentDate;
//    }
//}
