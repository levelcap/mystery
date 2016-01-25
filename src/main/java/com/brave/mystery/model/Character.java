package com.brave.mystery.model;

import org.springframework.data.annotation.Id;

public class Character {
    @Id
    private String id;
    private String name;
    private String url;
    private String description;
    private boolean npc;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNpc() {
        return npc;
    }

    public void setNpc(boolean npc) {
        this.npc = npc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;

        if (npc != character.npc) return false;
        if (description != null ? !description.equals(character.description) : character.description != null)
            return false;
        if (id != null ? !id.equals(character.id) : character.id != null) return false;
        if (name != null ? !name.equals(character.name) : character.name != null) return false;
        if (url != null ? !url.equals(character.url) : character.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (npc ? 1 : 0);
        return result;
    }
}