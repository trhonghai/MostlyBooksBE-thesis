package com.myshop.fullstackdemo.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("refresh_token")
    private String refresh_token;

    private List<String> roles;
    private long userId;

//    private long cartId;
//    private long userId;

}
