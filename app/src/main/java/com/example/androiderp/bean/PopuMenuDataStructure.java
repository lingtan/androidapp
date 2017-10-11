package com.example.androiderp.bean;

public class PopuMenuDataStructure {


    private int image;
    private String name;



    public PopuMenuDataStructure(int image,String name) {
        this.name = name;
        this.image=image;

    }


    public int getImage() {
        return image;
    }
    public String getName() {
        return name;
    }

}
