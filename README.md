## Java源码阅读（JDK1.8）
##### 创建于22-05-14 23:12:42
##### 阅读顺序参考于《Java面试指北》
* 1 java.lang 包下的基本包装类(Integer、Long、Double、Float 等)，还有字符串相关类(String、StringBuffer、StringBuilder 等)、常用类(Object、Exception、Thread、ThreadLocal等)。
* 2 java.lang.ref 包下的引用类(WeakReference、SoftReference 等)
* 3 java.lang.annotation 包下的注解的相关类
* 4 java.lang.reflect 包下的反射的相关类
* 5 java.util 包下为一些工具类，主要由各种容器和集合类(Map、Set、List 等)
* 6 java.util.concurrent 为并发包，主要是原子类、锁以及并发工具类
* 7 java.io 和 java.nio 可以结合着看 
* 8 java.time 主要包含时间相关的类，可以学习下 Java 8 新增的几个
* 9 java.net 包下为网络通信相关的类，可以阅读下 Socket 和 HTTPClient 相关代码

### 1.1 java.lang.Integer
1. 两种单例模式的构建（前提：私有化构造方法）
    ```java
   //第一种,在需要时通过.instance来实例化对象
    CharacterDataLatin1.instance;
   //...
    static final CharacterDataLatin1 instance = new CharacterDataLatin1();
    private CharacterDataLatin1() {};
   
   //第二种，通过getInstance()方法来构造
   //...
   public static Object instance;
   public static Object getInstance(){
        if(instance==null){
            instance=new Object();
        }
        return instance;
   }
    ```
    通过单例模式，一个类只可以创建一个实例，并且是通过自己创建该实例，创建后该实例将被供给给其他对象服务，除此以外，还有四种单例模式的构建方法。。。。此处不与赘述，等啥时候看到那了再分析。
2.
