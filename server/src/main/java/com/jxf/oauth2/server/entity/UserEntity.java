package com.jxf.oauth2.server.entity;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String description;
    private List<String> authorities;
}
