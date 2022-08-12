package com.risen.realtime.framework.kafka;

import com.alibaba.fastjson.JSON;
import com.risen.common.util.LogUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/8/11 13:54
 */
public class DataKafkaProducer {

    public static void sendSync(String url, String topic, String value) throws Exception {
        LogUtil.info("开始发送kafka数据url：{},topic:{},value:{}", url, topic, value);
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer(properties);
        Object object = kafkaProducer.send(new ProducerRecord<>(topic, value)).get();
        LogUtil.info("发送结果：{}", JSON.toJSONString(object));
        kafkaProducer.close();
    }


}
