package com.kq.nio.inetaddress;

import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost",INetAddressServer.PORT);
        socket.close();

    }

}
