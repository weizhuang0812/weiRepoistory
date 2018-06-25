package com.foriseland.fjf.mq.consumer.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.AbstractIterator;

public class KafkaConsumerRecords<K, V> implements Iterable<ConsumerRecord<K, V>>{

	 @SuppressWarnings("unchecked")
	    public static final ConsumerRecords<Object, Object> EMPTY = new ConsumerRecords<>(Collections.EMPTY_MAP);

	    private final Map<TopicPartition, List<ConsumerRecord<K, V>>> records;

	    public KafkaConsumerRecords(Map<TopicPartition, List<ConsumerRecord<K, V>>> records) {
	        this.records = records;
	    }

	    /**
	     * Get just the records for the given partition
	     * 
	     * @param partition The partition to get records for
	     */
	    public List<ConsumerRecord<K, V>> records(TopicPartition partition) {
	        List<ConsumerRecord<K, V>> recs = this.records.get(partition);
	        if (recs == null)
	            return Collections.emptyList();
	        else
	            return Collections.unmodifiableList(recs);
	    }

	    /**
	     * Get just the records for the given topic
	     */
	    public Iterable<ConsumerRecord<K, V>> records(String topic) {
	        if (topic == null)
	            throw new IllegalArgumentException("Topic must be non-null.");
	        List<List<ConsumerRecord<K, V>>> recs = new ArrayList<>();
	        for (Map.Entry<TopicPartition, List<ConsumerRecord<K, V>>> entry : records.entrySet()) {
	            if (entry.getKey().topic().equals(topic))
	                recs.add(entry.getValue());
	        }
	        return new ConcatenatedIterable<>(recs);
	    }

	    /**
	     * Get the partitions which have records contained in this record set.
	     * @return the set of partitions with data in this record set (may be empty if no data was returned)
	     */
	    public Set<TopicPartition> partitions() {
	        return Collections.unmodifiableSet(records.keySet());
	    }

	    @Override
	    public Iterator<ConsumerRecord<K, V>> iterator() {
	        return new ConcatenatedIterable<>(records.values()).iterator();
	    }

	    /**
	     * The number of records for all topics
	     */
	    public int count() {
	        int count = 0;
	        for (List<ConsumerRecord<K, V>> recs: this.records.values())
	            count += recs.size();
	        return count;
	    }

	    private static class ConcatenatedIterable<K, V> implements Iterable<ConsumerRecord<K, V>> {

	        private final Iterable<? extends Iterable<ConsumerRecord<K, V>>> iterables;

	        public ConcatenatedIterable(Iterable<? extends Iterable<ConsumerRecord<K, V>>> iterables) {
	            this.iterables = iterables;
	        }

	        @Override
	        public Iterator<ConsumerRecord<K, V>> iterator() {
	            return new AbstractIterator<ConsumerRecord<K, V>>() {
	                Iterator<? extends Iterable<ConsumerRecord<K, V>>> iters = iterables.iterator();
	                Iterator<ConsumerRecord<K, V>> current;

	                public ConsumerRecord<K, V> makeNext() {
	                    while (current == null || !current.hasNext()) {
	                        if (iters.hasNext())
	                            current = iters.next().iterator();
	                        else
	                            return allDone();
	                    }
	                    return current.next();
	                }
	            };
	        }
	    }

	    public boolean isEmpty() {
	        return records.isEmpty();
	    }

	    @SuppressWarnings("unchecked")
	    public static <K, V> ConsumerRecords<K, V> empty() {
	        return (ConsumerRecords<K, V>) EMPTY;
	    }

	


	

}
