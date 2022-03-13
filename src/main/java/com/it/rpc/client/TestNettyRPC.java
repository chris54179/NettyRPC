package com.it.rpc.client;

import com.it.rpc.clientStub.NettyRPCProxy;

public class TestNettyRPC {
    public static void main(String[] args) {
        HelloNetty helloNetty = (HelloNetty) NettyRPCProxy.create(HelloNetty.class);
        System.out.println(helloNetty.hello());

        HelloRPC helloRPC = (HelloRPC) NettyRPCProxy.create(HelloRPC.class);
        System.out.println(helloRPC.hello("RPC"));
    }
}
