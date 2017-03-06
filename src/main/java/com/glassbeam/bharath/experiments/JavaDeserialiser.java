package com.glassbeam.bharath.experiments;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by bharadwaj on 06/03/17.
 */
public abstract class JavaDeserialiser<T> implements Deserializer<T> {

    public void configure(Map<String, ?> configs, boolean isKey) {}

    public abstract T deserialize(String topic, byte[] data);

    @Override public void close() {}
}
