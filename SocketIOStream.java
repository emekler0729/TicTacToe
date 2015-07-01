package io.github.emekler0729.TicTacToe;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The SocketIOStream object manages a buffered input stream and an output stream to the Socket object
 * that is provided to the constructor.
 * <p>
 * The SocketIOStream has a Flow Control mode where the output stream is bInhibited after every transmission.
 * This prevents the SocketIOStream from flooding the output stream and gives the parent application control of
 * when the SocketIOStream is able to transmit to the Socket.
 *
 * @author      Eduard Mekler
 * @version     v0.1.0-beta
 * @since       2015-06-03
 */
public class SocketIOStream {
    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private boolean bInhibited;
    private boolean bFlowControl;

    SocketIOStream(InetSocketAddress adr, boolean enableFlowControl) throws IOException {
        bInhibited = true;
        bFlowControl = enableFlowControl;

        socket = new Socket();
        socket.connect(adr);

        out = new PrintWriter(socket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    SocketIOStream(InetSocketAddress adr) throws IOException {
        this(adr, false);
    }



    /**
     * Transmits a string to the SocketIOStream's output stream if the SocketIOStream
     * is not bInhibited. If Flow Control is enabled the SocketIOStream's output stream will be bInhibited
     * after the transmission is sent.
     *
     * @param s The string to be transmitted.
     */
    public void println(String s) {
        if(!bInhibited) {
            out.println(s);
            if(bFlowControl) {
                bInhibited = true;
            }
        }
    }

    /**
     * Reads the next line from the SocketIOStream's buffered input stream and returns
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
        return bFlowControl;
    }

    /**
     * Sets the driver's Flow Control state.
     *
     * @param enable A boolean indicating if Flow Control should be enabled.
     */
    public void setFlowControl(boolean enable) {
        bFlowControl = enable;
    }

    /**
     * Sets the driver's bInhibited state. If Flow Control is enabled then the parent application must disabled
     * the bInhibited state after every transmission when the SocketIOStream is permitted to transmit again.
     *
     * @param enable A boolean indicating if output inhibit should be enabled or disabled.
     */
    public void setInhibited(boolean enable) {
        bInhibited = enable;
    }

    public void close() throws IOException {
        socket.close();
    }
}