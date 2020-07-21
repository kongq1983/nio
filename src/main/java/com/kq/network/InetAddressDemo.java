package com.kq.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class InetAddressDemo {


    public static void main(String[] args) throws Exception{
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while(networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> enumeration = networkInterface.getInetAddresses();
            System.out.println("获得网络设备名称="+networkInterface.getName());
            System.out.println("获得网络设备显示名称="+networkInterface.getDisplayName());
            System.out.println("获得网络接口信息："+networkInterface.getDisplayName());
            while(enumeration.hasMoreElements()) {
                InetAddress inetAddress = enumeration.nextElement();
                System.out.println("getCanonicalHostName获取此IP的完全限定域名:"+inetAddress.getCanonicalHostName());
                System.out.println("getHostName获取此IP的主机名:"+inetAddress.getHostName());
                System.out.println("getHostAddress返回此InetAddress对象的原始IP地址:");

                byte[] address = inetAddress.getAddress();

                for(int i=0;i<address.length;i++){
                    System.out.print(address[i]+" ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();

        }

    }


}


