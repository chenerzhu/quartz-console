package com.chenerzhu.quartz.classLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyClassLoader extends ClassLoader {

    private static Map<String, byte[]> classMap = new ConcurrentHashMap<>();
    private String lib;
    private String classes;
    private String classPath;

    public MyClassLoader() {
    }

    public MyClassLoader(String classPath) {
        if (classPath.endsWith(File.separator)) {
            this.classPath = classPath;
        } else {
            this.classPath = classPath + File.separator;
        }
        lib = classPath + "lib" + File.separatorChar;
        classes = classPath + "classes" + File.separatorChar;
        preReadClassFile();
        preReadJarFile();
    }

    public static boolean addClass(String className, byte[] byteCode) {
        if (!classMap.containsKey(className)) {
            classMap.put(className, byteCode);
            return true;
        }
        return false;
    }

    /**
     * 这里仅仅卸载了myclassLoader的classMap中的class,虚拟机中的
     * Class的卸载是不可控的
     * 自定义类的卸载需要MyClassLoader不存在引用等条件
     * @param className
     * @return
     */
    public static boolean unloadClass(String className) {
        if (classMap.containsKey(className)) {
            classMap.remove(className);
            return true;
        }
        return false;
    }

    /**
     * 遵守双亲委托规则
     */
    @Override
    protected Class<?> findClass(String name) {
        try {
            byte[] result = getClass(name);
            if (result == null) {
                throw new ClassNotFoundException();
            } else {
                return defineClass(name, result, 0, result.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getClass(String className) {
        if (classMap.containsKey(className)) {
            return classMap.get(className);
        } else {
            return null;
        }
    }

    private void preReadClassFile() {
        File[] files = new File(classPath).listFiles();
        if (files != null) {
            for (File file : files) {
                scanClassFile(file);
            }
        }
    }

    private void scanClassFile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                try {
                    byte[] byteCode = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                    String className = file.getAbsolutePath().replace(classPath, "")
                            .replace(File.separator, ".")
                            .replace(".class", "");
                    addClass(className, byteCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    scanClassFile(f);
                }
            }
        }
    }

    private void preReadJarFile() {
        File[] files = new File(classPath).listFiles();
        if (files != null) {
            for (File file : files) {
                scanJarFile(file);
            }
        }
    }

    private void readJAR(JarFile jar) throws IOException {
        Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()) {
            JarEntry je = en.nextElement();
            je.getName();
            String name = je.getName();
            if (name.endsWith(".class")) {
                //String className = name.replace(File.separator, ".").replace(".class", "");
                String className = name.replace("\\", ".")
                        .replace("/", ".")
                        .replace(".class", "");
                InputStream input = null;
                ByteArrayOutputStream baos = null;
                try {
                    input = jar.getInputStream(je);
                    baos = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int bytesNumRead = 0;
                    while ((bytesNumRead = input.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesNumRead);
                    }
                    addClass(className, baos.toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (baos != null) {
                        baos.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                }
            }
        }
    }

    private void scanJarFile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                try {
                    readJAR(new JarFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    scanJarFile(f);
                }
            }
        }
    }


    public void addJar(String jarPath) throws IOException {
        File file = new File(jarPath);
        if (file.exists()) {
            JarFile jar = new JarFile(file);
            readJAR(jar);
        }
    }
}