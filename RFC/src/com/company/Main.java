package com.company;

import java.io.IOException;
import java.net.*;

public class Main {

    public static void main(String[] args) {

        String[] timeServers = new String[] {"gbg1.ntp.se","gbg2.ntp.se","mmo1.ntp.se"
                ,"mmo2.ntp.se","sth1.ntp.se","sth2.ntp.se","svl1.ntp.se","svl2.ntp.se"};

        for (String timeServer : timeServers) {

            DatagramSocket socket;
            DatagramPacket packet;

            try {
                socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(timeServer);
                SNTPMessage message = new SNTPMessage();
                byte[] buf = message.toByteArray();
                packet = new DatagramPacket(buf, buf.length, address, 123);

            } catch (SocketException e) {
                e.printStackTrace();
                continue;
            } catch (UnknownHostException e) {
                System.out.println("Host was not found");
                continue;
            }
            try {
                socket.setSoTimeout(1000);
                socket.send(packet);
                System.out.println("Sent request");
                socket.receive(packet);
                SNTPMessage response = new SNTPMessage(packet.getData());
                System.out.println(response);
                socket.close();
                break;

            } catch (SocketTimeoutException e) {
                System.out.println("Connection timeout for " + timeServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}