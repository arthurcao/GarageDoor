package com.kooltechs.garagedoor.dns_api;

import android.util.Log;

import java.net.InetAddress;

import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;
import org.xbill.mDNS.Lookup;
import org.xbill.mDNS.MulticastDNSService;
import org.xbill.mDNS.ServiceInstance;
import org.xbill.mDNS.ServiceName;

public class DNSService {

    public static void test(){
        int priority = 10;
        int weight = 10;
        int port = 8080;
        String ucn = "urn:smpte:ucn:org.smpte.st2071:device_v1.0";
        String ucnPTRName = "_org.smpte.st2071:device_v1.0._sub._mdc._tcp";
//        String ucnPTRName = "_mdc._tcp";
        String domain = "local.";
        String hostname = "TestHost";

        try
        {
            MulticastDNSService service = new MulticastDNSService();

            Name fqn = new Name(hostname + "." + (domain.endsWith(".") ? domain : domain + "."));
            ServiceName srvName = new ServiceName(hostname + "." + ucnPTRName + "." + (domain.endsWith(".") ? domain : domain + "."));

            ServiceInstance serviceInstance = new ServiceInstance(srvName, priority, weight, port, fqn, new InetAddress[] {InetAddress.getByName("192.168.1.74")}, "textvers=1", "rn=urn:smpte:udn:local:id=1234567890ABCDEF", "proto=mdcp", "path=/Device");
            ServiceInstance registeredService = service.register(serviceInstance);
            if (registeredService != null){
                Log.e("test", "Services Successfully Registered: \n\t" + registeredService);
            } else
            {
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

                Log.e("test","Services Registration for UCN \"" + ucn + "\" in domain \"" + domain + "\" Failed!");
                if (hostnameResolves)
                {
                    Log.e("test","Hostname \"" + fqn + "\" can be resolved.");
                } else
                {
                    Log.e("test","Hostname \"" + fqn + "\" cannot be resolved!");
                }
            }

            while (true)
            {
                Thread.sleep(10);
                if (System.in.read() == 'q')
                {
                    break;
                }
            }

            if (service.unregister(registeredService))
            {
                Log.e("test", "Services Successfully Unregistered: \n\t" + registeredService);
            } else
            {
                Log.e("test","Services was not Unregistered: \n\t" + registeredService);
            }

            service.close();
            System.exit(0);
        } catch (Exception e)
        {
            Log.e("test","Error Registering Capability \"" + ucn + "\" for Discovery - " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
