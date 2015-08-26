package com.incar.convert;

import com.incar.enums.DataType;
import com.incar.exception.ConversionException;
import com.incar.utils.DataBuilder;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFlower<T> {
    private Class<T> melClass;
    private static Map<Class<?>,List<DataBee>> melMap=new HashMap<Class<?>, List<DataBee>>();
    Logger log=Logger.getLogger(DataFlower.class);
    public DataFlower(Class<T> melClass){
        this.melClass=melClass;
    }

    public T bytesToEntity(ByteBuffer buffer){
        if(melClass.getAnnotation(DataEntity.class)!=null){
            //第一步，通过DataMel实体构建DataBee集合
            List<DataBee> beeList=buildBeeList();
            //第二步，通过DataBee集合获取数据
            return loadEntityData(buffer, beeList);
        }
        else{
            throw new ConversionException("类型"+melClass+"必须声明DataMel注解");
        }
    }

    public ByteBuffer entityToBytes(T mel){
        if(this.melClass.equals(mel.getClass())&&mel.getClass().getAnnotation(DataEntity.class)!=null){
            //第一步，通过DataMel实体构建DataBee集合
            List<DataBee> beeList=buildBeeList();
            //第二步，通过DataBee集合获取数据
            return loadByteData(mel,beeList);
        }
        else{
            throw new ConversionException("类型"+melClass+"必须声明DataMel注解");
        }
    }
    private ByteBuffer loadByteData(T mel,List<DataBee> beeList) {
        DataBuilder builder=DataBuilder.build();
        try {
            for(DataBee bee:beeList){
                Object value=bee.getGetter().invoke(mel);
                if(bee.getDataType().equals(DataType.STRING)) builder.putString(String.valueOf(value));
                if(bee.getDataType().equals(DataType.U_INT_8)) builder.putUInt8BE((Short)value);
                if(bee.getDataType().equals(DataType.U_INT_16))builder.putUInt16BE((Integer)value);
                if(bee.getDataType().equals(DataType.U_INT_32))builder.putUInt32BE((Long)value);
                if(bee.getDataType().equals(DataType.INT_8)) builder.putInt8BE((Short)value);
                if(bee.getDataType().equals(DataType.INT_16))builder.putInt16BE((Integer)value);
                if(bee.getDataType().equals(DataType.INT_32)) builder.putInt32BE((Integer) value);
                if(bee.getDataType().equals(DataType.BYTE)) builder.putByte((Byte) value);

            }
            return builder.buffer();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    private T loadEntityData(ByteBuffer buffer,List<DataBee> beeList) {
        DataBuilder builder=DataBuilder.build(buffer);
        try {
            //1、实例化JavaBean对象
            T bean=melClass.newInstance();
            for(DataBee bee:beeList){
                Object value=null;
                if(bee.getDataType().equals(DataType.STRING)) value=builder.getString();
                if(bee.getDataType().equals(DataType.U_INT_8)) value=builder.getUInt8();
                if(bee.getDataType().equals(DataType.U_INT_16)) value=builder.getUInt16BE();
                if(bee.getDataType().equals(DataType.U_INT_32)) value=builder.getUInt32BE();
                if(bee.getDataType().equals(DataType.INT_8)) value=builder.getInt8();
                if(bee.getDataType().equals(DataType.INT_16)) value=builder.getInt16BE();
                if(bee.getDataType().equals(DataType.INT_32)) value=builder.getInt32BE();
                if(bee.getDataType().equals(DataType.BYTE)) value=builder.getByte();
                bee.getSetter().invoke(bean,value);
            }
            return bean;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DataBee> buildBeeList() {
        if(DataFlower.melMap.containsKey(melClass)){
            return DataFlower.melMap.get(melClass);
        }
        else{
            try {
                List<DataBee> beeList=new ArrayList<DataBee>(10);
                List<DataBee> orderList=new ArrayList<DataBee>();
                Field[] fields=melClass.getDeclaredFields();
                for(Field f:fields){
                    //System.out.println(f.getName());
                    //4、找到所有的set开头的方法
                    Class<?> fieldType= f.getType();
                    String fieldName=f.getName();
                    String methodSetterName="set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                    String methodGetterName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                    Method ms=melClass.getMethod(methodSetterName,fieldType);
                    Method mg=melClass.getMethod(methodGetterName);
                    //5、根据这些set开头的方法推算出对应的属性名
                    DataMel mthAnn=ms.getAnnotation(DataMel.class);
                    DataMel colAnn=f.getAnnotation(DataMel.class);
                    if(mthAnn!=null&&colAnn!=null)throw new ConversionException("无法同时在属性及属性对应的set方法上指定@DataMel注解");
                    DataType type=DataType.AUTO;
                    int order=-1;
                    if(mthAnn!=null){
                        type=mthAnn.value();
                        order=mthAnn.order();
                    }
                    else if(colAnn!=null){
                        type=colAnn.value();
                        order=colAnn.order();
                    }
                    type=detectDataType(type,fieldType);
                    if(order>=0){
                        orderList.add(new DataBee(mg,ms,type,order));
                    }
                    else{
                        beeList.add(new DataBee(mg,ms,type));
                    }

                }
                for(DataBee dg:orderList){
                    beeList.add(dg.getOrder(),dg);
                }
                for(int i=0;i<beeList.size();i++){
                    beeList.get(i).setOrder(i);
                }
                DataFlower.melMap.put(melClass,beeList);
                return beeList;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private DataType detectDataType(DataType type,Class<?> fieldType) {
        if(type==null)type=DataType.AUTO;
        if(type.equals(DataType.AUTO)){
            if(fieldType.equals(Byte.class)||fieldType.equals(Boolean.class))return DataType.BYTE;
            if(fieldType.equals(Short.class))return DataType.U_INT_8;
            if(fieldType.equals(Integer.class))return DataType.U_INT_16;
            if(fieldType.equals(Long.class))return DataType.U_INT_32;
            if(fieldType.equals(String.class))return DataType.STRING;
            throw new ConversionException("暂不支持的类型"+fieldType);
        }
        else{
            return type;
        }
    }


    private static void printByteBuffer(ByteBuffer bb){
        bb.flip();
        for(int i=0;i<bb.limit();i++){
            String byteStr=Integer.toHexString(bb.get()).toUpperCase();
            if(byteStr.length()==1)byteStr="0"+byteStr;
            if(byteStr.length()!=2)byteStr=byteStr.substring(byteStr.length()-2);
            System.out.print(byteStr+" ");
        }
    }
   /* public static void main(String[] args){
        ByteBuffer bb=ByteBuffer.allocate(1024);
        String[] command="AA 55 00 4B FF B4 00 05 16 03 49 4E 43 41 52 30 30 30 30 30 33 00 00 00 00 3D 37 00 4C 53 56 46 56 36 31 38 33 44 32 32 30 30 39 33 32 00 56 31 2E 35 30 2E 30 30 00 56 30 2E 30 30 2E 30 30 00 56 33 2E 31 33 2E 31 35 00 FF 01 0F 22".split(" ");
        byte[] abc=new byte[command.length];
        for(int i=0;i<command.length;i++){
            abc[i]=Integer.valueOf(command[i],16).byteValue();
        }
        bb.put(abc);
        bb.flip();
        //System.out.println(bb.position()+"-"+bb.limit()+"-"+bb.capacity());
        DataFlower<Req1603> df=new DataFlower<Req1603>(Req1603.class);
        Req1603 req=df.bytesToEntity(bb);
        ByteBuffer bbb=df.entityToBytes(req);
        System.out.println(req);
        printByteBuffer(bbb);
    }*/
}
