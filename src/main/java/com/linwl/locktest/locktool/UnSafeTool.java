package com.linwl.locktest.locktool;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @program: locktest
 * @description: 魔法类工具类
 * @author: linwl
 * @create: 2020-07-24 14:00
 **/
public class UnSafeTool {

    public static Unsafe getUnSafe(){
        try{
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        }
        catch (Exception e){
            return null;
        }
    }
}
