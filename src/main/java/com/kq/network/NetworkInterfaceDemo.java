package com.kq.network;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkInterfaceDemo {

    public static void main(String[] args) throws Exception{
        Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();

        while (interfaceEnumeration.hasMoreElements()) {
            NetworkInterface networkInterface = interfaceEnumeration.nextElement();
            System.out.println("获得网络设备名称="+networkInterface.getName());
            System.out.println("获得网络设备显示名称="+networkInterface.getDisplayName());
            System.out.println("获得最大传输单元="+networkInterface.getMTU());
            System.out.print("获得网卡的物理地址=");
            // 十进制需要转换16进制  比如-66   256-66=190=BE
            byte[] bytes = networkInterface.getHardwareAddress();
            if(bytes!=null && bytes.length >0) {
                for (int i = 0; i < bytes.length; i++) {
                    System.out.println(bytes[i]+" ");
                }
                System.out.println();
            }

            System.out.println();
        }

    }

}
