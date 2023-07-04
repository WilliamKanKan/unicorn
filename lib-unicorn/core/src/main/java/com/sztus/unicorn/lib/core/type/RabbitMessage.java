package com.sztus.unicorn.lib.core.type;

import java.io.Serializable;
import java.util.Date;

public class RabbitMessage implements Serializable {

    private String producer;
    private String method;
    private String data;
    private Date createdAt;

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}