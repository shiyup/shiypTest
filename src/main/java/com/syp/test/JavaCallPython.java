package com.syp.test;

import org.python.util.PythonInterpreter;

/**
 * Created by shiyuping on 2024/5/8 15:57
 *
 * @author shiyuping
 */
public class JavaCallPython {

    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();
        //Python环境路径
        //我在这里使用相对路径，注意区分
        interpreter.execfile("/Users/mac/PycharmProjects/PythonProject/image/all.py");
    }
}
