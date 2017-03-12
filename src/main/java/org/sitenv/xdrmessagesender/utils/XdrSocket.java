package org.sitenv.xdrmessagesender.utils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

/**
 * Created by Brian on 3/1/2017.
 */
public class XdrSocket {
    public static String sendMessage(URL endpoint, String payload) throws IOException {
        int port = endpoint.getPort();
        if (port == -1) {
            port = 80;
        }
        InetAddress addr = InetAddress.getByName(endpoint.getHost());
        Socket socket = new Socket(addr, port);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

        bufferedWriter.write(payload);
        bufferedWriter.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return getResponse(bufferedReader);
    }

    private static String getResponse(BufferedReader bufferedReader) throws IOException {
        StringBuilder response = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line + "\n");
        }
        return response.toString();
    }
}
