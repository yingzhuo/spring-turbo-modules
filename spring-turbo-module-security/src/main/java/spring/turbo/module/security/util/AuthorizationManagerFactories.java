/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.util;

import org.springframework.lang.Nullable;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;

import java.util.Objects;

/**
 * {@link AuthorizationManager} 创建用工具类
 *
 * @author 应卓
 * @since 2.0.4
 */
public final class AuthorizationManagerFactories {

    /**
     * 私有构造方法
     */
    private AuthorizationManagerFactories() {
        super();
    }

    public static <T> AuthorizationManager<T> authenticated() {
        return authenticated(null);
    }

    public static <T> AuthorizationManager<T> authenticated(@Nullable AuthenticationTrustResolver authenticationTrustResolver) {
        final var trustResolver = enforceAuthenticationTrustResolver(authenticationTrustResolver);
        return (authentication, object) -> {
            final var auth = authentication.get();
            return new AuthorizationDecision(
                    auth != null &&
                            auth.isAuthenticated() &&
                            !trustResolver.isAnonymous(auth)
            );
        };
    }

    public static <T> AuthorizationManager<T> fullyAuthenticated() {
        return fullyAuthenticated(null);
    }

    public static <T> AuthorizationManager<T> fullyAuthenticated(@Nullable AuthenticationTrustResolver authenticationTrustResolver) {
        final var trustResolver = enforceAuthenticationTrustResolver(authenticationTrustResolver);
        return (authentication, object) -> {
            final var auth = authentication.get();
            return new AuthorizationDecision(
                    auth != null && !trustResolver.isAnonymous(auth)
                            && auth.isAuthenticated() &&
                            !trustResolver.isRememberMe(auth)
            );
        };
    }

    public static <T> AuthorizationManager<T> anonymous() {
        return anonymous(null);
    }

    public static <T> AuthorizationManager<T> anonymous(@Nullable AuthenticationTrustResolver authenticationTrustResolver) {
        final var trustResolver = enforceAuthenticationTrustResolver(authenticationTrustResolver);
        return (authentication, object) -> {
            final var auth = authentication.get();
            return new AuthorizationDecision(
                    auth != null &&
                            trustResolver.isAnonymous(auth)
            );
        };
    }

    public static <T> AuthorizationManager<T> rememberMe() {
        return rememberMe(null);
    }

    public static <T> AuthorizationManager<T> rememberMe(@Nullable AuthenticationTrustResolver authenticationTrustResolver) {
        final var trustResolver = enforceAuthenticationTrustResolver(authenticationTrustResolver);
        return (authentication, object) -> {
            final var auth = authentication.get();
            return new AuthorizationDecision(
                    auth != null &&
                            trustResolver.isRememberMe(auth)
            );
        };
    }

    public static <T> AuthorizationManager<T> hasRole(String role) {
        return hasRole(null, role);
    }

    public static <T> AuthorizationManager<T> hasRole(@Nullable RoleHierarchy roleHierarchy, String role) {
        roleHierarchy = enforceRoleHierarchy(roleHierarchy);
        final var manager = AuthorityAuthorizationManager.<T>hasRole(role);
        manager.setRoleHierarchy(roleHierarchy);
        return manager;
    }

    public static <T> AuthorizationManager<T> hasAuthority(String authority) {
        return hasAuthority(null, authority);
    }

    public static <T> AuthorizationManager<T> hasAuthority(@Nullable RoleHierarchy roleHierarchy, String authority) {
        roleHierarchy = enforceRoleHierarchy(roleHierarchy);
        final var manager = AuthorityAuthorizationManager.<T>hasAuthority(authority);
        manager.setRoleHierarchy(roleHierarchy);
        return manager;
    }

    public static <T> AuthorizationManager<T> hasAnyRole(String... roles) {
        return hasAnyRole(null, roles);
    }

    public static <T> AuthorizationManager<T> hasAnyRole(@Nullable RoleHierarchy roleHierarchy, String... roles) {
        roleHierarchy = enforceRoleHierarchy(roleHierarchy);
        final var manager = AuthorityAuthorizationManager.<T>hasAnyRole(roles);
        manager.setRoleHierarchy(roleHierarchy);
        return manager;
    }

    public static <T> AuthorizationManager<T> hasAnyAuthority(String... authorities) {
        return hasAnyAuthority(null, authorities);
    }

    public static <T> AuthorizationManager<T> hasAnyAuthority(@Nullable RoleHierarchy roleHierarchy, String... authorities) {
        roleHierarchy = enforceRoleHierarchy(roleHierarchy);
        final var manager = AuthorityAuthorizationManager.<T>hasAnyAuthority(authorities);
        manager.setRoleHierarchy(roleHierarchy);
        return manager;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static AuthenticationTrustResolver enforceAuthenticationTrustResolver(@Nullable AuthenticationTrustResolver resolver) {
        return Objects.requireNonNullElse(
                resolver,
                new AuthenticationTrustResolverImpl()
        );
    }

    private static RoleHierarchy enforceRoleHierarchy(@Nullable RoleHierarchy roleHierarchy) {
        return Objects.requireNonNullElse(
                roleHierarchy,
                new NullRoleHierarchy()
        );
    }
}
