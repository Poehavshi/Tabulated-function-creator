package com.company.gui;

import java.io.FileInputStream;
import java.io.IOException;

public class FunctionClassLoader extends ClassLoader{

    public Class loadClassFromFile(String fileName) throws IOException {
        byte[] b = loadClassData(fileName);
        return defineClass(null, b, 0, b.length);
    }

    private byte[] loadClassData(String filename) throws IOException {
        FileInputStream input = new FileInputStream(filename);
        byte[] fileContent = new byte[input.available()];
        input.read(fileContent);
        input.close();
        return fileContent;
    }

}
