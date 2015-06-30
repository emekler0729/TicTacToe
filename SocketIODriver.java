package io.github.emekler0729.TicTacToe;

import java.io.*;
import java.net.Socket;

/**
 * The SocketIODriver object manages a buffered input stream and an output stream to the Socket object
 * that is provided to the constructor.
 * <p>
 * The SocketIODriver has a Flow Control mode where the output stream is inhibited after every transmission.
 * This prevents the SocketIODriver from flooding the output stream and gives the parent application control of
 * when the SocketIODriver is able to transmit to the Socket.
 *
 * @author      Eduard Mekler
 * @version     v0.1.0-beta
 * @since       2015-06-03
 */
public class SocketIODriver {
    private PrintWriter out;
    private BufferedReader in;

    private boolean inhibited = true;
    private boolean flowControl = false;

    /**
     * Instantiates a SocketIODriver object with an output stream to and buffered input stream from
     * the provided Socket object. Flow control is disabled by default.
     *
     * @param socket The Socket object which the I/O streams will communicate to.
     * @throws IOException An IOException will be thrown if either of the streams are unable to be established.
     */
    SocketIODriver(Socket socket) throws IOException {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        finally {

        }
    }

    /**
     * Instantiates a SocketIODriver object with an output stream to and buffered input stream from
     * the provided Socket Object.
     *
     * @param socket The Socket object which the I/O streams will communicate to.
     * @param enableFlowControl A boolean indicating if Flow Control is be enabled.
     * @throws IOException An IOException will be thrown if either of the streams are unable to be established.
     */
    SocketIODriver(Socket socket, boolean enableFlowControl) throws IOException {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        finally {

        }

        flowControl = enableFlowControl;
    }

    /**
     * Transmits a string to the SocketIODriver's output stream if the SocketIODriver
     * is not inhibited. If Flow Control is enabled the SocketIODriver's output stream will be inhibited
     * after the transmission is sent.
     *
     * @param s The string to be transmitted.
     */
    public void println(String s) {
        if(!inhibited) {
            out.println(s);
            if(flowControl) {
                inhibited = true;
            }
        }
    }

    /**
     * Reads the next line from the SocketIODriver's buffered input stream and returns
     * the read string.
     *
     * @return The next string from the buffered input stream's FIFO queue. If an Exception occurs the String
     * "_EXCEPTION" is returned.
     */
    public String readLine() {
        String s;
        try {
            s = in.readLine();
            return s;
        }

        catch(IOException e) {
            return "_EXCEPTION";
        }
    }

    /**
     * Returns a boolean indicating if Flow Control is enabled.
     * @return A boolean indicating if Flow Control is enabled.
     */
    public boolean getFlowControl() {
        return flowControl;
    }

    /**
     * Sets the driver's Flow Control state.
     *
     * @param enable A boolean indicating if Flow Control should be enabled.
     */
    public void setFlowControl(boolean enable) {
        flowControl = enable;
    }

    /**
     * Sets the driver's inhibited state. If Flow Control is enabled then the parent application must disabled
     * the inhibited state after every transmission when the SocketIODriver is permitted to transmit again.
     *
     * @param enable A boolean indicating if output inhibit should be enabled or disabled.
     */
    public void setInhibited(boolean enable) {
        inhibited = enable;
    }
}