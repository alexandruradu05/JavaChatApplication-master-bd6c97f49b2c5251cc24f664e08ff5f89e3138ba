/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seriaf.poo.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import seriaf.poo.structs.Message;
import seriaf.poo.structs.PrivateMessage;

/**
 *
 * @author professor
 */
public class ClientPeer extends Thread {

    private final ObjectOutputStream mObjectStream;
    private final String mSender;
    private final Socket mSocket;

    public ClientPeer(String sender, Socket communicationSocket) throws IOException {
        mSender = sender;
        mObjectStream = new ObjectOutputStream(communicationSocket.getOutputStream());
        mSocket = communicationSocket;
    }

    @Override
    public void run () {
         try {
            ObjectInputStream stream = new ObjectInputStream(mSocket.getInputStream());

            while (true) {
                Message message = (Message) stream.readObject();
                System.out.println(message.toString());
                //System.out.println(stream.readObject());
            }
        } catch (EOFException ex) {
            // client disconnected gracefully so do nothing
        } catch (IOException ex) {
            System.err.println("Client connection reset: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("Unknown object received.");
        } finally {
            System.exit(0);
        }
    }
    
    public void sendMessage(String message) throws IOException {
        mObjectStream.writeObject(new Message(mSender, message));
    }

    public void sendMessage(String recipient, String message) throws IOException {
        mObjectStream.writeObject(new PrivateMessage(recipient, mSender, message));
    }
}
