package zm.mc.core.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import zm.mc.CainBuilderPlugin;

public class ClassScanner {
    private static final LoggerUtil logger = LoggerUtil.INSTANCE;

    private ClassScanner() {
        // Private constructor to prevent instantiation
    }

    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        try {
            logger.info("Scanning path: " + path);
            Enumeration<URL> resources =  CainBuilderPlugin.class.getClassLoader().getResources(path);
            logger.info("Found resources for hasMoreElements: " + resources.hasMoreElements());
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File directory = new File(resource.getFile());
                    if (directory.exists()) {
                        classes.addAll(findClasses(directory, packageName));
                    }
                } else if (resource.getProtocol().equals("jar")) {
                    JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                    try (JarFile jarFile = jarURLConnection.getJarFile()) {
                        classes.addAll(findClassesInJar(jarFile, path));
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("Failed to scan classes in package: " + packageName);
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                try {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    logger.severe("Class not found: " + e.getMessage());
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> findClassesInJar(JarFile jarFile, String path) {
        List<Class<?>> classes = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(path) && name.endsWith(".class") && !entry.isDirectory()) {
                String className = name.replace('/', '.').substring(0, name.length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    logger.severe("Class not found in JAR: " + e.getMessage());
                }
            }
        }
        return classes;
    }
}