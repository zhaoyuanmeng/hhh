package com.xaxc.teqin.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginDTO {

    @NonNull
    private String userName;

    @NonNull
    private String password;
}
