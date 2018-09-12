package com.stream.examples;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class WordCountImpl implements WordCount {
    private Properties props = new Properties();
    private String bootstrap = "localhost:9092";
    private String appId = "WordCount";

    public WordCountImpl(String bootstrap, String appId){
        this.bootstrap = bootstrap;
        this.appId = appId;
    }

    @Override
    public void start() {
        final StreamsBuilder source = new StreamsBuilder();
        final Topology topology = source.build();

        KStream<String, String> lines = source.stream("input-stream");
        lines
            .flatMapValues((key, values) -> Arrays.asList(values.toLowerCase().split("\\W+")))
            .groupByKey()
            .count()
            .toStream()
            .to("output-stream", Produced.with(Serdes.String(), Serdes.Long()));
        ;

        final KafkaStreams streams = new KafkaStreams(topology, this.props);
        streams.start();
    }

    @Override
    public void configure() {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,appId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap);

        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
    }

    @Override
    public void shutdown() {

    }
}
