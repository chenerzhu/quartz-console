package com.chenerzhu.quartz.test;

import com.chenerzhu.quartz.classLoader.MyClassLoader;
import com.chenerzhu.quartz.common.Hello;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author chenerzhu
 * @create 2018-10-03 10:44
 **/
public class ClassLoaderTest {

    @Test
    public void classLoaderTest() throws Exception {
        MyClassLoader myClassLoader = new MyClassLoader("C:\\Users\\chenerzhu\\Desktop");
        //Class<?> c1 = myClassLoader.loadClass("com.chenerzhu.quartz.common.Test");
        //Class<?> c1 = Class.forName("com.chenerzhu.quartz.common.Test", true, myClassLoader);

        /*MyClassLoader myClassLoader=new MyClassLoader();
        byte[] bytes=Files.readAllBytes(Paths.get("C:\\Users\\chenerzhu\\Desktop\\com\\chenerzhu\\quartz\\common\\Test.class"));
        myClassLoader.addClass("com.chenerzhu.quartz.common.Test",bytes);*/
        Class<?> c1 = myClassLoader.loadClass("com.chenerzhu.quartz.common.Test");
        Object obj = c1.newInstance();
        System.out.println(obj);
        System.out.println(obj.getClass().getClassLoader());
        myClassLoader.unloadClass("com.chenerzhu.quartz.common.Test");
        c1 = myClassLoader.loadClass("com.chenerzhu.quartz.common.Test");
        Object obj1 = c1.newInstance();
        System.out.println(obj1);
        System.out.println(obj1.getClass().getClassLoader());
    }

    @Test
    public void javassistTest() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        //cp.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

        /*//classPath加载
        cp.insertClassPath("C:\\Users\\chenerzhu\\Desktop");
        CtClass cc1=cp.getCtClass("com.chenerzhu.quartz.common.Test");
        System.out.println(cc1.toClass().newInstance());*/

        /*//字节流makeClass
        FileInputStream fileInputStream=new FileInputStream("C:\\Users\\chenerzhu\\Desktop\\com\\chenerzhu\\quartz\\common\\Test.class");
        CtClass cc2=cp.makeClass(fileInputStream);
        System.out.println(cc2.toClass().newInstance());*/

        //获取已存在class,字节码修改
        CtClass cc = cp.getCtClass("com.chenerzhu.quartz.common.Hello");
        //cp.makeClass("com.chenerzhu.quartz.common.Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
        //cc.writeFile("./");
        Class c = cc.toClass(Thread.currentThread().getContextClassLoader(),null);
        Hello h = (Hello)c.newInstance();
        h.say();
    }
}