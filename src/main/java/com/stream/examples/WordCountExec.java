package com.stream.examples;

import org.apache.kafka.common.KafkaException;

public class WordCountExec {
    public static void main(String[] args) {
        WordCount wc = new WordCountImpl("localhost:9092", "wordcount");
        wc.configure();


        try {
            wc.start();
        }
        catch (KafkaException e){
            e.printStackTrace();
            wc.shutdown();
        }
        finally {
            wc.shutdown();
        }
    }
}
