package com.example.trpg_writer.entity;

import java.io.Serializable;
import java.util.Objects;

public class SceneInfoId implements Serializable {
    private Integer scene;
    private Integer info;

    public SceneInfoId() {
    }

    public SceneInfoId(Integer scene, Integer info) {
        this.scene = scene;
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneInfoId that = (SceneInfoId) o;
        return Objects.equals(scene, that.scene) &&
               Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scene, info);
    }

    // Getters and Setters
    public Integer getScene() {
        return scene;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public Integer getInfo() {
        return info;
    }

    public void setInfo(Integer info) {
        this.info = info;
    }
}