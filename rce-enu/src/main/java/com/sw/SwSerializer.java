package com.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class First implements Serializable{
    private String name;
    public void setName(final String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    // 打印验证
    @Override
    public String toString(){
        return getClass().getName() + ",name=" + this.name;
    }
}
class Second implements Serializable{
    private String email;
    public void setEmail(final String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    // 打印验证
    @Override
    public String toString(){
        return getClass().getName() + ",email=" + this.email;
    }
}
class Third implements Serializable{
    private int age;
    public void setAge(final int age){
        this.age = age;
    }
    public int getAge(){
        return this.age;
    }
    // 打印验证
    @Override
    public String toString(){
        return getClass().getName() + ",age=" + String.valueOf(this.age);
    }
}

/**
 * Java序列化多个对象到一个文件
 */
public class SwSerializer {

    public static void write(final Object dataObj, final String filename) throws Exception{
        final File file = new File(filename);
        // 文件是否存在，存在的时候需要截取头部aced 0005的值
        if(file.exists()){
            final FileOutputStream fo = new FileOutputStream(file, true);
            final ObjectOutputStream oos = new ObjectOutputStream(fo);
            long pos = 0;
            if(file.exists()){
                pos = fo.getChannel().position() - 4;  // 追加的时候需要去掉头部的aced 0005字节
                fo.getChannel().truncate(pos);
            }
            oos.writeObject(dataObj);   // 追加序列化
        }else{
            // 文件不存在
            file.createNewFile();
            final FileOutputStream fo = new FileOutputStream(file);
            final ObjectOutputStream oos = new ObjectOutputStream(fo);
            oos.writeObject(dataObj);   // 直接序列化
            oos.close();
        }
    }

    public static List<Object> read(final String filename) throws Exception{
        List<Object> obj = new ArrayList<>();
        final File file = new File(filename);
        if(file.exists()){
            final FileInputStream fn = new FileInputStream(file);
            final ObjectInputStream ois = new ObjectInputStream(fn);
            while(fn.available() > 0){  // 代表文件还有内容
                Object reference = ois.readObject(); // 从流中读取所有对象到一个Array
                if(null != reference){
                    obj.add(reference);
                }
            }
        }
        return obj;
    }

    public static void main(String args[]) throws Exception{
        // 1.构造几个不同的对象
        final First obj = new First();
        obj.setName("Lang");
        final Second obj2 = new Second();
        obj2.setEmail("lang.yu@hpe.com");
        final Third obj3 = new Third();
        obj3.setAge(31);

        // 2.写入对象到同一个文件
        final String filename = "object.bin";
        // 程序重新执行，如果文件存在则删除
        final File file = new File(filename);
        if(file.exists()){
            file.delete();
        }
        // 序列化对象到同一个文件
        SwSerializer.write(obj, filename);
        SwSerializer.write(obj2, filename);
        SwSerializer.write(obj3, filename);
        obj.setName("Lang2");
        SwSerializer.write(obj, filename);  // 修改Object1 过后追加

        // 3.从文件中读取所有对象
        final List<Object> references = SwSerializer.read(filename);
        for(final Object reference: references){
            // 打印文件中读取到的每一个对象
            System.out.println(reference);
        }
    }
}
