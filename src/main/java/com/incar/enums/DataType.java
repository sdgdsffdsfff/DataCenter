package com.incar.enums;


public enum DataType {
    AUTO,       //自动判断
    BYTE,       //单字节，对应类型Byte,Boolean
    INT_8,      //单字节整数，对应类型Short,Integer,Long
    INT_16,     //双字节整数，对应类型Short,Integer,Long
    INT_32,     //四字节整数，对应类型Integer,Long
    U_INT_8,    //无符号单字节整数，对应类型Short,Integer,Long
    U_INT_16,   //无符号双字节整数，对应类型Integer,Long
    U_INT_32,   //无符号四字节整数，对应类型Long
    CHAR,       //定长字符串型，对应类型Char[],String，编码方式ASCII
    STRING      //变长字符串型，对应类型String。与CHAR的区别是末尾多一个字节0，而且可以设置字符集
}
