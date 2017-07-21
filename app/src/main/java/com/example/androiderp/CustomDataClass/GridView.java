package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class GridView extends DataSupport {
    private int id;
    private String name;
    private int choiceImage;
    private int image;
    private boolean Choice;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getChoiceImage() {
        return choiceImage;
    }

    public void setChoiceImage(int choiceImage) {
        this.choiceImage = choiceImage;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isChoice() {
        return Choice;
    }

    public void setChoice(boolean choice) {
        Choice = choice;
    }
}
