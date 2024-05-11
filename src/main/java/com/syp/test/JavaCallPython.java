package com.syp.test;

import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by shiyuping on 2024/5/8 15:57
 *
 * @author shiyuping
 */
public class JavaCallPython {

//    public static void main(String[] args) {
//        PythonInterpreter interpreter = new PythonInterpreter();
//        //Python环境路径
//        //我在这里使用相对路径，注意区分
//        interpreter.execfile("/Users/mac/PycharmProjects/PythonProject/image/all.py");
//    }

    public static void main(String[] args) {
        String[] args1 = new String[]{"E:\\Program\\Anaconda3\\python.exe", "D:/Program/Python/videoRec.py", "1", "2"};
        System.out.println("result=" + callPython(args1));
    }

    private static String callPython(String... param) {
        String result = "";
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(param);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line)) {
                    result = line;
                }
            }
            System.out.println("waitFor=" + process.waitFor());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
