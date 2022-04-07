package com.syp.test.netty.demo.chat.protocol;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 用于扩展序列化、反序列化算法
 */
public interface Serializer {

    /**
     * 反序列化方法
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * 序列化方法
     * @param object
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T object);

    enum Algorithm implements Serializer {

        Java {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败", e);
                }
            }

            @Override
            public <T> byte[] serialize(T object) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }
        },

        Json {
            @Override
            @SneakyThrows
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                ObjectMapper customMapper = new ObjectMapper();
                return customMapper.readValue(new String(bytes, StandardCharsets.UTF_8), clazz);
            }

            @Override
            @SneakyThrows
            public <T> byte[] serialize(T object) {
                ObjectMapper customMapper = new ObjectMapper();
                return customMapper.writeValueAsString(object).getBytes(StandardCharsets.UTF_8);
            }
        },

        Hessian{
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                if (bytes == null) {

                    throw new NullPointerException();

                }

                T result;

                try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {

                    HessianSerializerInput hessianInput = new HessianSerializerInput(is);

                    result = (T) hessianInput.readObject(clazz);

                } catch (Exception e) {

                    throw new SerializationException(e);

                }

                return result;
            }

            @Override
            public <T> byte[] serialize(T object) {
                if (object == null) {

                    throw new NullPointerException();

                }

                byte[] results;

                HessianSerializerOutput hessianOutput;

                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

                    hessianOutput = new HessianSerializerOutput(os);

                    hessianOutput.writeObject(object);

                    hessianOutput.flush();

                    results = os.toByteArray();

                } catch (Exception e) {

                    throw new SerializationException(e);

                }

                return results;
            }
        }
    }
}