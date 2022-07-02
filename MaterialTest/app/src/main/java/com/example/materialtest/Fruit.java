package com.example.materialtest;

public class Fruit {
    private String name; //表示水果的名字
    private int imageId; //表示水果对应的图片资源

    public Fruit(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName(){return name;}

    public int getImageId() {
        return imageId;
    }
}
