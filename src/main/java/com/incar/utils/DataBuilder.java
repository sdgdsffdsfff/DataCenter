package com.incar.utils;


import com.incar.exception.ConversionException;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class DataBuilder {
    private static final long UNSIGNED_INTEGER_MAX_VALUE=(long)Integer.MAX_VALUE+Integer.MAX_VALUE+1;
    private static final long REVERSE_UNSIGNED_INTEGER=(long)Integer.MIN_VALUE+Integer.MIN_VALUE;
    private static final String DEFAULT_CHARSET="GBK";
    private boolean readMode;
    private ByteBuffer buffer;
    private DataBuilder(ByteBuffer buffer){
        this.buffer=buffer;
        this.readMode=false;
        buffer.rewind();
    }
    public ByteBuffer buffer(){
        return this.buffer;
    }
    /**
     * 工厂构造方法
     * @param buffer    缓存类,暂只支持ByteBuffer
     * @return  DataBuilder实例
     */
    public static DataBuilder build(ByteBuffer buffer){
        return new DataBuilder(buffer);
    }
    public static DataBuilder build(){
        return new DataBuilder(ByteBuffer.allocate(1024));
    }
    /**
     * 开启读模式
     */
    private void turnReadMode(){
        if(!readMode){
            buffer.rewind();
            readMode=true;
        }
    }

    /**
     * 开启写模式
     */
    private void turnWriteMode(){
        if(readMode){
            buffer.flip();
            readMode=false;
        }
    }
    /**
     * 获取一个字节
     * @return 单字节
     */
    public Byte getByte(){
        turnReadMode();
        return buffer.get();
    }
    /**
     * 获取无符号单字节整型
     * @return  整型数
     */
    public Short getUInt8(){
        turnReadMode();
        Integer temp=Byte.toUnsignedInt(buffer.get());
        return temp.shortValue();
    }

    /**
     * 获取单字节整型
     * @return  整型数
     */
    public Short getInt8(){
        turnReadMode();
        Byte temp=buffer.get();
        return temp.shortValue();
    }
    /**
     * 获取大端无符号双字节整型
     * @return  整型数
     */
    public int getUInt16BE(){
        turnReadMode();
        return Short.toUnsignedInt(buffer.getShort());
    }
    /**
     * 获取大端双字节整型
     * @return  整型数
     */
    public Integer getInt16BE(){
        turnReadMode();
        return (int)buffer.getShort();
    }
    /**
     * 获取大端无符号四字节整型
     * @return  长整型数
     */
    public Long getUInt32BE(){
        turnReadMode();
        return Integer.toUnsignedLong(buffer.getInt());
    }

    /**
     * 获取大端四字节整型
     * @return  整型数
     */
    public Integer getInt32BE(){
        turnReadMode();
        return buffer.getInt();
    }

    /**
     * 写入一个字节
     * @param b 一个byte
     */
    public void putByte(Byte b){
        turnWriteMode();
        buffer.put(b);
    }

    /**
     * 写入大端单字节整型
     * @param num   整型数
     */
    public void putInt8BE(Short num){
        turnWriteMode();
        byte a=(byte)num.shortValue();
        buffer.put(a);
    }

    /**
     * 写入大端无符号单字节整型
     * @param num   整型数
     */
    public void putUInt8BE(Short num){
        turnWriteMode();
        byte a=(byte)Short.toUnsignedInt(num);
        buffer.put(a);
    }

    /**
     * 写入大端双字节整型
     * @param num   整型数
     */
    public void putInt16BE(Integer num){
        turnWriteMode();
        buffer.putShort(num.shortValue());
    }

    /**
     * 写入大端无符号双字节整型
     * @param num   整型数
     */
    public void putUInt16BE(Integer num){
        turnWriteMode();
        buffer.putShort(Long.valueOf(Integer.toUnsignedLong(num)).shortValue());
    }

    /**
     * 写入大端四字节整型
     * @param num   整型数
     */
    public void putInt32BE(Integer num){
        turnWriteMode();
        buffer.putInt(num);
    }
    /**
     * 写入大端无符号四字节整型
     * @param num   整型数
     */
    public void putUInt32BE(Long num){
        int intNum;
        if(num>UNSIGNED_INTEGER_MAX_VALUE){
            throw new ConversionException("数据["+num+"]过大，无法转换成为无符号整型");
        }
        else if(num<=Integer.MAX_VALUE){
            intNum=num.intValue();
        }
        else{
            Long temp=REVERSE_UNSIGNED_INTEGER+num;
            intNum=temp.intValue();
        }
        turnWriteMode();
        buffer.putInt(intNum);
    }

    /**
     * 写入字符串，末尾补0
     * @param str   字符串
     */
    public void putString(String str){
        try {
            byte[] strBytes = str.getBytes(DEFAULT_CHARSET);
            turnWriteMode();
            buffer.put(strBytes);
            byte zero=0;
            buffer.put(zero);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ConversionException("字符串"+str+"无法转换成byte数组");
        }
    }

    /**
     * 获取字符串
     * @return  字符串
     */
    public String getString(){
        turnReadMode();
        List<Byte> list=new ArrayList<Byte>();
        byte b;
        do{
            b=buffer.get();
            list.add(b);
        }while(b!=0);
        byte[] byteList=new byte[list.size()-1];
        for(int i=0;i<list.size()-1;i++){
            byteList[i]=list.get(i);
        }
        String a;
        try {
            a = new String(byteList,DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ConversionException("byte数组无法转换成字符串");
        }
        return a;
    }/*
    public static void main(String[] args){
        DataBuilder db=DataBuilder.build();
        db.putString("你好0");
        String a=db.getString();
        System.out.println(a);
    }*/
}
