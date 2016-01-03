package com.brave.mystery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Set;

public class Order {
    @Id
    private String id;
    private String date;
    private String userId;
    private Set<ItemRequest> requestedItems;
    private boolean filled = false;
    private boolean sent = false;

    @Transient
    private User user;

    public Order() {
    }

    public Order(String date, String userId, Set<ItemRequest> requestedItems) {
        this.date = date;
        this.userId = userId;
        this.requestedItems = requestedItems;
        this.filled = false;
        this.sent = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<ItemRequest> getRequestedItems() {
        return requestedItems;
    }

    public void setRequestedItems(Set<ItemRequest> requestedItems) {
        this.requestedItems = requestedItems;
    }

    public void addItemRequest(ItemRequest item) {
        this.requestedItems.add(item);
    }

    public void removeItemRequest(ItemRequest item) {
        this.requestedItems.remove(item);
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", requestedItems=" + requestedItems +
                ", filled=" + filled +
                '}';
    }
}
