package com.social.meli.permission.profile;

import org.springframework.stereotype.Component;

@Component("sellerPermission")
public class SellerPermission implements ProfilePermission {
    @Override
    public boolean canPost() {return true;}
    @Override
    public boolean canFollow() {return false;}

}
