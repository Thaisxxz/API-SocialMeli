package com.social.meli.permission.profile;
import com.social.meli.exception.follower.UnauthorizedException;

public interface ProfilePermission {
    boolean canPost();
    boolean canFollow();

    default void ensureCanFollow() {
        if (!canFollow()) {
            throw new UnauthorizedException("Este perfil não pode seguir vendedores.");
        }
    }
    default void ensureCanBeFollowed() {
        if (canFollow()) {
            throw new UnauthorizedException("Apenas perfil vendedor pode ser seguido.");
        }
    }
    default void ensureCanPost() {
        if (!canPost()) {
            throw new UnauthorizedException("Esse perfil não pode fazer publicações, você precisa estar no perfil de vendedor (SELLER).");
        }

    }
}
