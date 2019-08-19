package org.graalvm.demos.micronaut.service.api.v1;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Introspected
public class Todo {
    private @NotBlank String content;
    private String id;
    private Date timeCreated;
    private Date lasTimeUpdated;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getLasTimeUpdated() {
        return lasTimeUpdated;
    }

    public void setLasTimeUpdated(Date lasTimeUpdated) {
        this.lasTimeUpdated = lasTimeUpdated;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", timeCreated=" + timeCreated +
                ", lasTimeUpdated=" + lasTimeUpdated +
                '}';
    }
}
