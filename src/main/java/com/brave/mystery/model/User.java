package com.brave.mystery.model;

import org.springframework.data.annotation.Id;

public class User {
	@Id
	private String id;
	private String email;
	private String password;
    private String location;
    private UserRole role;

    public enum UserRole {
        REQUESTER, MANAGER
    }

	public User() {
	}

	public User(String email, String password, String location, UserRole role) {
		this.email = email;
		this.password = password;
        this.location = location;
        this.role = role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", role=" + role +
                '}';
    }
}
