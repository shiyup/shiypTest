package com.syp.test;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shiyuping on 2024/6/6 13:33
 *
 * @author shiyuping
 */
@Slf4j
public class JavaCallShell {

    public static void main2(String[] args) {
        try {
            // 执行Shell命令
            Process process = Runtime.getRuntime().exec("bash -vn /Users/mac/test.sh");
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Execute: " + line);
            }
            BufferedReader errorReader=new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while((errorLine = errorReader.readLine()) != null) {
                System.out.println("Error: " + errorLine);
            }
            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //execute();
        //check();
        execute2();
    }

    public static void execute(){
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "/Users/mac/test.sh");
        try {
            // 执行Shell命令
            Process process = processBuilder.start();
            // 读取命令输出
            BufferedReader reader=new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            BufferedReader errorReader=new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));
            String errorLine;
            while((errorLine = errorReader.readLine()) != null) {
                System.out.println("Error: " + errorLine);
            }
            // 等待命令执行完成 0-success
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void check(){
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-vn", "/Users/mac/test.sh");
        try {
            Process process = processBuilder.start();
            BufferedReader errorReader=new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));
            String errorLine;
            while((errorLine = errorReader.readLine()) != null) {
                System.out.println(errorLine);
            }
            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);
            errorReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void execute2(){
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "/Users/mac/test2.sh");
        List<String> logLines = Lists.newArrayList();
        try {
            //合并标准输出流和标准错误流
            processBuilder.redirectErrorStream(true);
            // 执行Shell命令
            Process process = processBuilder.start();
            // 读取命令输出
            try (BufferedReader inReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = inReader.readLine()) != null) {
                    logLines.add(line);
                }
            }
            // 等待命令执行完成 0-success
            int exitCode = process.waitFor();
            log.info("Exit code: {}", exitCode);
            log.info("log info start >>>>>>>>>>>>>>");
            log.info(logLines.stream().collect(Collectors.joining(System.lineSeparator())));
            log.info("log info end >>>>>>>>>>>>>>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
