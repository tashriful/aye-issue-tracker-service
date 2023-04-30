package com.aye.issueTracker.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "Attachment")
public class Attachment {

    @Id
    private ObjectId id;
    private String fileName;
    private String contentType;
    private long size;
    private byte[] content;

    public Attachment() {
    }

    public Attachment(ObjectId id, String fileName, String contentType, long size, byte[] content) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
