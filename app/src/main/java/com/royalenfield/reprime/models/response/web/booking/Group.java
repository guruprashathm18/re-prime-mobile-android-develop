package com.royalenfield.reprime.models.response.web.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javiergonzalezcabezas on 11/5/15.
 */
public class Group<T> {

    public String string;
    public final List<T> children = new ArrayList<T>();

    public Group(String string) {
        this.string = string;
    }

}