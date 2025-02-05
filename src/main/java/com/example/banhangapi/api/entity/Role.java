package com.example.banhangapi.api.entity;

public class Role {
    private ROLES id;
    private String name;

    public Role(ROLES id) {
        this.id = id;
        if(id==ROLES.ROLE_ADMIN){
            this.name = "ROLES_ADMIN";
        }else{
            this.name = "ROLES_USER";
        }
    }

    public String getName() {
        return name;
    }

}
