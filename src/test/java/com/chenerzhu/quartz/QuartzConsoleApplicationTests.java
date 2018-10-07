package com.chenerzhu.quartz;

import com.chenerzhu.quartz.job.dynamic.DynamicSample;
import javassist.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzConsoleApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
    public void testJavassist() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool pool = ClassPool.getDefault();
        pool.clearImportedPackages();

        //pool.appendClassPath("com.chenerzhu.quartz.job.dynamic");
        //pool.insertClassPath("com.chenerzhu.quartz.job.dynamic");
        pool.insertClassPath(new ClassClassPath(DynamicSample.class));
        //pool.makeClass(DynamicSample.class.getClass().getName());
        //pool.insertClassPath("./classes/com/chenerzhu/quartz/job/dynamic");
        CtClass ctClass=pool.get(DynamicSample.class.getClass().getName());
        Runnable runnable=(Runnable)ctClass.toClass().newInstance();
        new Thread(runnable).start();
    }

}
