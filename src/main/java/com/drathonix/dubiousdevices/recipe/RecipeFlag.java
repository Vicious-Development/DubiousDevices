package com.drathonix.dubiousdevices.recipe;

import org.bukkit.Material;

import java.util.Objects;

public class RecipeFlag {
    public String name;
    public String description;
    public boolean defaultState = false;
    public Material material;

    public RecipeFlag(String name, String desc, Material mat) {
        this.name=name;
        description=desc;
        material=mat;
    }
    public RecipeFlag(String name, String desc ,Material mat, boolean defaultState) {
        this(name,desc,mat);
        this.defaultState=defaultState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeFlag that = (RecipeFlag) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
