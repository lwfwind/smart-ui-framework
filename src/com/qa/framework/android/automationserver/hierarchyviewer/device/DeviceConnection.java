/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qa.framework.android.automationserver.hierarchyviewer.device;

import com.android.ddmlib.IDevice;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * This class is used for connecting to a device in debug mode running the view
 * server.
 */
public class DeviceConnection {

    // Now a socket channel, since socket channels are friendly with interrupts.
    private SocketChannel socketChannel;

    private BufferedReader in;

    private BufferedWriter out;

    /**
     * Instantiates a new Device connection.
     *
     * @param device the device
     * @throws IOException the io exception
     */
    public DeviceConnection(IDevice device) throws IOException {
        socketChannel = SocketChannel.open();
        int port = DeviceBridge.getDeviceLocalPort(device);

        if (port == -1) {
            throw new IOException();
        }

        socketChannel.connect(new InetSocketAddress("127.0.0.1", port));
        socketChannel.socket().setSoTimeout(120000);
    }

    /**
     * Gets input stream.
     *
     * @return the input stream
     * @throws IOException the io exception
     */
    public BufferedReader getInputStream() throws IOException {
        if (in == null) {
            in = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream()));
        }
        return in;
    }

    public InputStream getInStream() throws IOException {
        return socketChannel.socket().getInputStream();
    }

    /**
     * Gets output stream.
     *
     * @return the output stream
     * @throws IOException the io exception
     */
    public BufferedWriter getOutputStream() throws IOException {
        if (out == null) {
            out = new BufferedWriter(new OutputStreamWriter(socketChannel.socket()
                    .getOutputStream()));
        }
        return out;
    }

    /**
     * Gets socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socketChannel.socket();
    }

    /**
     * Send command.
     *
     * @param command the command
     * @throws IOException the io exception
     */
    public void sendCommand(String command) throws IOException {
        BufferedWriter out = getOutputStream();
        out.write(command);
        out.newLine();
        out.flush();
    }

    /**
     * Close.
     */
    public void close() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
        }
        try {
            socketChannel.close();
        } catch (IOException e) {
        }
    }
}
