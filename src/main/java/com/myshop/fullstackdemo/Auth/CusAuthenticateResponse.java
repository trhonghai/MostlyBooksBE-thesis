package com.myshop.fullstackdemo.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class CusAuthenticateResponse extends AuthenticationResponse {
    private long cartId;
    private long userId;

    // Constructor with parameters
    public CusAuthenticateResponse(String access_token, String refresh_token, long cartId, long userId) {
        super(access_token, refresh_token, null , userId);
        this.cartId = cartId;
        this.userId = userId;
    }
}
