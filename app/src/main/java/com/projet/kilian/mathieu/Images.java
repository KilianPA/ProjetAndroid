package com.projet.kilian.mathieu;

public class Images {

    private String path;
    private int id;

    public Images(){}

    public Images(String path1, String name1){
        this.path = path1;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}