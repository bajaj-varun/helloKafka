import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.scala.Serdes;

import java.util.Properties;

public class HelloKafka {

    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-test");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("input-words");

       Topology topology = builder.build();
       /*  System.out.print(topology);

        KStream<String, String> dest = builder.stream("output-words");*/

       source.foreach((key, value) -> System.out.println(key+" = "+value));
       final KafkaStreams streams = new KafkaStreams(topology, prop);
       streams.start();
    }
}
