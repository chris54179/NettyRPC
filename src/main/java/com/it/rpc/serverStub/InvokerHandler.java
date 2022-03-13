package com.it.rpc.serverStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class InvokerHandler extends ChannelInboundHandlerAdapter {
    private String getImplClassName(ClassInfo classInfo) throws ClassNotFoundException {
        String interfacePath = "com.it.rpc.server";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);
        Class superClass = Class.forName(interfacePath + interfaceName);
        Reflections reflections = new Reflections(interfacePath);

        Set<Class> ImplClassSet = reflections.getSubTypesOf(superClass);
        if (ImplClassSet.size() == 0) {
            System.out.println("未找到實現類");
            return null;
        } else if (ImplClassSet.size() > 1) {
            System.out.println("找到多個實現類，未明確使用哪一個");
            return null;
        } else {
            Class[] classes = ImplClassSet.toArray(new Class[0]);
            return classes[0].getName();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }
}
