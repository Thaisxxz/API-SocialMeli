package com.social.meli.permission.profile;

import com.social.meli.ENUM.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProfilePermissionFactory {

    private final Map<String, ProfilePermission> permissionMap;

    @Autowired
    public ProfilePermissionFactory(Map<String, ProfilePermission> permissionMap) {
        this.permissionMap = permissionMap;
    }
    public ProfilePermission getProfile(ProfileType type){
        return permissionMap.get(type.name().toLowerCase()+ "Permission");
    }

}
