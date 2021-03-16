package com.syp.test.algorithm;

import java.util.*;

/**
 * Created by shiyuping on 2021/3/3 10:01 下午
 * 负载均衡算法
 */
public class LoadBalancing {

    static Map<String,Integer> serverWeightMap = new HashMap<String,Integer>();
    static Integer pos = 0;
    static{
        serverWeightMap.put("192.168.1.100",1);
        serverWeightMap.put("192.168.1.101",1);
        serverWeightMap.put("192.168.1.102",4);
        serverWeightMap.put("192.168.1.103",1);
        serverWeightMap.put("192.168.1.104",1);
        serverWeightMap.put("192.168.1.105",3);
        serverWeightMap.put("192.168.1.106",1);
        serverWeightMap.put("192.168.1.107",2);
        serverWeightMap.put("192.168.1.108",1);
        serverWeightMap.put("192.168.1.109",1);
        serverWeightMap.put("192.168.1.110",3);
    }

    //轮询
    public static String testRoundRobin(){
        //重新创建一个map,避免出现由于服务器上线和下线导致的并发问题
        Map<String,Integer> serverMap = new HashMap<String, Integer>();
        serverMap.putAll(serverWeightMap);

        //取得ip地址list
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);
        String server = null;
        synchronized (pos){
            if(pos > keySet.size()){
                pos = new Integer(0);
            }
            server = keyList.get(pos);
            pos++;
        }
        return server;

    }

    //随机
    public static String testRandom(){
        //重新创建一个map,避免出现由于服务器上线和下线导致的并发问题
        Map<String,Integer> serverMap = new HashMap<String,Integer>();
        serverMap.putAll(serverWeightMap);


        //取得ip地址list
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);

        Random random = new Random();
        int randomPos = random.nextInt(keyList.size());

        String server = keyList.get(randomPos);
        return server;
    }

    //源地址哈希（hash）法
    public static String testConsumerHash(String remoteIp){
        //重新创建一个map,避免出现由于服务器上线和下线导致的并发问题
        Map<String,Integer> serverMap = new HashMap<String,Integer>();
        serverMap.putAll(serverWeightMap);


        //取得ip地址list
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);

        int hashCode = remoteIp.hashCode();
        int serverListSize = keyList.size();
        int serverPos = hashCode % serverListSize;

        return keyList.get(serverPos);
    }

    //加权轮询
    public static String testWeightRoundRobin(){
        //重新创建一个map，避免由于服务器上线和下线导致的并发问题
        Map<String,Integer> serverMap = new HashMap<String,Integer>();
        serverMap.putAll(serverWeightMap);

        //取得ip地址list
        Set<String> keySet = serverMap.keySet();
        Iterator<String> it = keySet.iterator();

        List<String> serverList = new ArrayList<String>();
        while(it.hasNext()){
            String server = it.next();
            Integer weight = serverMap.get(server);
            for(int i=0;i<weight;i++){
                serverList.add(server);
            }
        }

        String server = null;
        synchronized (pos){
            if(pos >= serverList.size()){
                pos = new Integer(0);
            }
            server = serverList.get(pos);
            pos++;
        }
        return server;
    }

    //加权随机
    public static String testWeightRandom(){
        //重新创建一个map,避免出现由于服务器上线和下线导致的并发问题
        Map<String,Integer> serverMap = new HashMap<String,Integer>();
        serverMap.putAll(serverWeightMap);


        //取得ip地址list
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> serverList = new ArrayList<String>();

        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()){
            String server = iterator.next();
            Integer weight = serverMap.get(server);
            for(int i=0;i<weight;i++){
                serverList.add(server);
            }
        }

        Random random = new Random();
        int randomPos = random.nextInt(serverList.size());
        String server = serverList.get(randomPos);

        return server;
    }
}
