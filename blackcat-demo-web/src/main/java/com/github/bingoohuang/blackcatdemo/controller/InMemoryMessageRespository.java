package com.github.bingoohuang.blackcatdemo.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryMessageRespository {

    private static AtomicLong counter = new AtomicLong();

    private final ConcurrentMap<Long, Message> messages = new ConcurrentHashMap<Long, Message>();

    public Iterable<Message> findAll() {
        return this.messages.values();
    }

    public Message save(Message message) {
        Long id = message.getId();
        if (id == null) {
            id = counter.incrementAndGet();
            message.setId(id);
        }
        this.messages.put(id, message);
        return message;
    }

    public Message findMessage(Long id) {
        return this.messages.get(id);
    }

}