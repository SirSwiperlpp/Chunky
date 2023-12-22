package de.realityrift.chunky.API;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ntpFetcher
{
    public static String getNtpTime(String ntpServer, String targetTimeZone) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ntpServer);
            long currentTime = getTimeFromNtpServer(inetAddress);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone(targetTimeZone));

            return sdf.format(new Date(currentTime));
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Abrufen der NTP-Zeit";
        }
    }

    private static long getTimeFromNtpServer(InetAddress ntpServer) throws Exception {
        long ntpEpochDiff = 2208988800L;

        byte[] data = new byte[48];
        data[0] = 0x1B;

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(data, data.length, ntpServer, 123);
        socket.send(packet);


        packet = new DatagramPacket(new byte[48], 48);
        socket.receive(packet);

        long ntpTime = 0;
        for (int i = 0; i < 4; i++) {
            ntpTime = (ntpTime << 8) | (packet.getData()[40 + i] & 0xff);
        }

        socket.close();

        return ntpTime - ntpEpochDiff * 1000;
    }

}
