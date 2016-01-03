package com.brave.mystery.model;

import org.springframework.data.annotation.Id;

import java.util.Set;

/**
 * Created by dcohen on 7/21/15.
 */
public class Item {
    @Id
    private String id;
    private String name;
    private String description;
    private Set<String> sizeTypes;
    private boolean available;

    public Item() {}

    public Item(String name, String description, Set<String> sizeTypes, boolean available) {
        this.name = name;
        this.description = description;
        this.sizeTypes = sizeTypes;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getSizeTypes() {
        return sizeTypes;
    }

    public void setSizeTypes(Set<String> sizeTypes) {
        this.sizeTypes = sizeTypes;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
