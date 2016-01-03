package com.brave.mystery.model;

/**
 * Created by dcohen on 7/21/15.
 */
public class ItemRequest {
    private Item item;
    private int number;
    private String sizeType;

    public ItemRequest() {

    }

    public ItemRequest(Item item, int number, String sizeType) {
        this.item = item;
        this.number = number;
        this.sizeType = sizeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemRequest that = (ItemRequest) o;

        if (item != null ? !item.equals(that.item) : that.item != null) return false;

        return true;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    @Override
    public int hashCode() {
        return item != null ? item.hashCode() : 0;
    }

    @Override
    public String toString() {
        return number + " " + sizeType + " " + item;
    }
}
