package org.yolkin.dto;

public class UserCreationDTO {
    private String name;

    public UserCreationDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
