package de.realityrift.chunky.API;

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

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
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

        java.net.DatagramSocket socket = new java.net.DatagramSocket();
        java.net.DatagramPacket packet = new java.net.DatagramPacket(data, data.length, ntpServer, 123);
        socket.send(packet);
        socket.receive(packet);

        long ntpTime = 0;
        for (int i = 0; i < 4; i++) {
            ntpTime = (ntpTime << 8) | (data[40 + i] & 0xff);
        }

        return ntpTime - ntpEpochDiff * 1000;
    }
}
