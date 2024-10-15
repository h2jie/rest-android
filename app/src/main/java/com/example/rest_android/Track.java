package com.example.rest_android;
import java.util.UUID;

public class Track {
    private String id;
    private String title;
    private String singer;

    public Track(String id, String title, String singer) {
        if (id == null || id.isEmpty()) {
            // 如果 id 为空，生成一个随机的 UUID
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        this.title = title;
        this.singer = singer;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}