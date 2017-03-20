package it.tesi.prochilo.notifiche;

/**
 * All messages from the user have a specific format.
 * The Action field defines, what the action is about. An example
 * is the action com.grokkingandroid.sampleapp.samples.gcm.action.REGISTER, 
 * used to tell the server about a newly registered user.
 * <br>
 * Any further fields are specific for the given action.
 */
public interface PayloadProcessor {
    
    void handleMessage(CcsMessage msg);
    
}
