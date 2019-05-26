package com.projet.kilian.mathieu;

public class Images {

    private String path;
    private String name;
    private int id;

    public Images(){}

    public Images(String path1, String name1){
        this.path = path1;
        this.name = name1;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}