package com.kooltechs.garagedoor.dns_api;

import android.util.Log;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xbill.mDNS.Lookup;
import org.xbill.mDNS.MulticastDNSService;
import org.xbill.mDNS.ServiceInstance;
import org.xbill.mDNS.ServiceName;

import java.io.IOException;
import java.net.InetAddress;

public class DNSService {

    private MulticastDNSService mMulticastDNSService;
    private ServiceInstance mServiceInstance;

    private DNSService INSTANCE = new DNSService();
    private DNSService(){
    }

    public DNSService getInstance(){
        return INSTANCE;
    }

    public void register(){
        unregister();
        int priority = 10;
        int weight = 10;
        int port = 8080;
        String ucn = "urn:smpte:ucn:org.smpte.st2071:device_v1.0";
        String ucnPTRName = "_org.smpte.st2071:device_v1.0._sub._mdc._tcp";
//        String ucnPTRName = "_mdc._tcp";
        String domain = "local.";
        String hostname = "TestHost";
        try {
            Name fqn = new Name(hostname + "." + (domain.endsWith(".") ? domain : domain + "."));
            ServiceName srvName = new ServiceName(hostname + "." + ucnPTRName + "." + (domain.endsWith(".") ? domain : domain + "."));

            mMulticastDNSService = new MulticastDNSService();
            ServiceInstance serviceInstance = new ServiceInstance(srvName, priority, weight, port, fqn, new InetAddress[] {InetAddress.getByName("192.168.1.74")}, "textvers=1", "rn=urn:smpte:udn:local:id=1234567890ABCDEF", "proto=mdcp", "path=/Device");
            mServiceInstance = mMulticastDNSService.register(serviceInstance);
            if (mServiceInstance != null){
                Log.e("test", "Services Successfully Registered:" + mServiceInstance);
            }else{
                Log.e("test","Services fail Registered");
                boolean hostnameResolves = false;

                Lookup lookup = new Lookup(fqn);
                Record[] rrs = lookup.lookupRecords();
                if (rrs != null && rrs.length > 0)
                {
                    outer:
                    for (Record rr : rrs)
                    {
                        switch (rr.getType())
                        {
                            case Type.A:
                            case Type.A6:
                            case Type.AAAA:
                            case Type.DNAME:
                            case Type.CNAME:
                            case Type.PTR:
                                if (rr.getName().equals(fqn))
                                {
                                    hostnameResolves = true;
                                    break outer;
                                }
                                break;
                        }
                    }
                }

                Log.e("test", "Services Registration for UCN \"" + ucn + "\" in domain \"" + domain + "\" Failed!");
                if (hostnameResolves){
                    Log.e("test", "Hostname \"" + fqn + "\" can be resolved.");
                } else{
                    Log.e("test", "Hostname \"" + fqn + "\" cannot be resolved!");
                }
            }


        } catch (TextParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void unregister(){
        if(mMulticastDNSService != null && mServiceInstance != null){
            try {
                if (mMulticastDNSService.unregister(mServiceInstance))  {
                    System.out.println("Services Successfully Unregistered: \n\t" + mServiceInstance);
                } else  {
                    System.err.println("Services was not Unregistered: \n\t" + mServiceInstance);
                }
                mMulticastDNSService.close();
                mMulticastDNSService = null;
                mServiceInstance = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void test(){
        int priority = 10;
        int weight = 10;
        int port = 8080;
        String ucn = "urn:smpte:ucn:org.smpte.st2071:device_v1.0";
        String ucnPTRName = "_org.smpte.st2071:device_v1.0._sub._mdc._tcp";
//        String ucnPTRName = "_mdc._tcp";
        String domain = "local.";
        String hostname = "TestHost";
        try {
            Name fqn = new Name(hostname + "." + (domain.endsWith(".") ? domain : domain + "."));
            ServiceName srvName = new ServiceName(hostname + "." + ucnPTRName + "." + (domain.endsWith(".") ? domain : domain + "."));
            MulticastDNSService service = new MulticastDNSService();
            ServiceInstance serviceInstance = new ServiceInstance(srvName, priority, weight, port, fqn, new InetAddress[] {InetAddress.getByName("192.168.1.74")}, "textvers=1", "rn=urn:smpte:udn:local:id=1234567890ABCDEF", "proto=mdcp", "path=/Device");
            ServiceInstance registeredService = service.register(serviceInstance);
            if (registeredService != null){
                Log.e("test","Services Successfully Registered:" + registeredService);
            }else{
                Log.e("test","Services fail Registered");
                boolean hostnameResolves = false;

                Lookup lookup = new Lookup(fqn);
                Record[] rrs = lookup.lookupRecords();
                if (rrs != null && rrs.length > 0)
                {
                    outer:
                    for (Record rr : rrs)
                    {
                        switch (rr.getType())
                        {
                            case Type.A:
                            case Type.A6:
                            case Type.AAAA:
                            case Type.DNAME:
                            case Type.CNAME:
                            case Type.PTR:
                                if (rr.getName().equals(fqn))
                                {
                                    hostnameResolves = true;
                                    break outer;
                                }
                                break;
                        }
                    }
                }

                Log.e("test", "Services Registration for UCN \"" + ucn + "\" in domain \"" + domain + "\" Failed!");
                if (hostnameResolves){
                    Log.e("test", "Hostname \"" + fqn + "\" can be resolved.");
                } else{
                    Log.e("test", "Hostname \"" + fqn + "\" cannot be resolved!");
                }
            }

            while (true)  {
                Thread.sleep(10);
                if (System.in.read() == 'q') {
                    break;
                }
            }

            if (service.unregister(registeredService))  {
                System.out.println("Services Successfully Unregistered: \n\t" + registeredService);
            } else  {
                System.err.println("Services was not Unregistered: \n\t" + registeredService);
            }

            service.close();
        } catch (TextParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
