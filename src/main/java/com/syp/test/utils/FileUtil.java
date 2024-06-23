package com.syp.test.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by shiyuping on 2024/6/6 14:01
 *
 * @author shiyuping
 */
public class FileUtil {

    public static void main(String[] args) throws IOException {
        String filePath = "/Users/mac/Downloads/excel截图6.png";
        File file = new File(filePath);
        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        FileTime creationTime= attrs.creationTime();
        // 将FileTime转换为Instant
        Instant instant = creationTime.toInstant();
        // 将Instant转换为ZonedDateTime，并指定时区（北京时间为UTC+8）
        ZonedDateTime beijingTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        // 格式化时间输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedBeijingTime = beijingTime.format(formatter);
        System.out.println(formattedBeijingTime);
    }

}
