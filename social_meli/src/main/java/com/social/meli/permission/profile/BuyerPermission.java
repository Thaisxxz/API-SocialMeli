package com.social.meli.permission.profile;

import org.springframework.stereotype.Component;

@Component("buyerPermission")
public class BuyerPermission implements ProfilePermission {
    @Override
    public boolean canPost() {return false;}
    @Override
    public boolean canFollow() {return true;}
}
