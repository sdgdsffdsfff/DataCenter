package com.incar.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class TestBuffer {
    /*public static void main(String[] args) throws UnsupportedEncodingException {
        ByteBuffer bb=ByteBuffer.allocate(1024);
        String[] command="AA 36 36 FF C9 55 05 16 03 4C 41 4E 44 55 30 30 30 30 31 30 00 00 00 00 34 34 31 00 00 56 31 2E 36 31 00 56 31 2E 30 2E 30 00 56 33 2E 31 36 2E 36 32 00 FF 00 0A B1".split(" ");
        byte[] abc=new byte[command.length];
        for(int i=0;i<command.length;i++){
            abc[i]=Integer.valueOf(command[i],16).byteValue();
        }
        *//*String a=new String(abc,"GBK");
        System.out.println(a);*//*

        System.out.println(bb.position()+"-"+bb.limit()+"-"+bb.capacity());
        bb.put(abc);
        bb.flip();
        System.out.println(bb.position()+"-"+bb.limit()+"-"+bb.capacity());
        //System.out.println(ByteBuilder.getShort(bb));
        //System.out.println(ByteBuilder.getWord(bb));
        *//*bb.put("你好".getBytes("GBK"));
        bb.putShort((short)0);
        System.out.println(bb.get());*//*
    }*/
}
