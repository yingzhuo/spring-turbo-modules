/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Objects;

/**
 * {@link AuthorizationManager} 创建用工具类
 *
 * @author 应卓
 * @since 2.0.4
 */
public final class AuthorizationManagerFactories {

    private static final RoleHierarchy DEFAULT_ROLE_HIERARCHY = new NullRoleHierarchy();

    private static final AuthenticationTrustResolver DEFAULT_AUTHENTICATION_TRUST_RESOLVER = new AuthenticationTrustResolverImpl();

    /**
     * 私有构造方法
     */
    private AuthorizationManagerFactories() {
        super();
    }

    /**
     * 返回授权器, 任何请求都可以获得授权
     *
     * @param <T> 授权器实例
     * @return 授权器实例
     */
    public static <T> AuthorizationManager<T> permitAll() {
        return (authentication, object) -> new AuthorizationDecision(true);
    }

    /**
     * 返回授权器, 任何请求都不能获得授权
     *
     * @param <T> 授权器实例
     * @return 授权器实例
     */
    public static <T> AuthorizationManager<T> denyAll() {
        return (authentication, object) -> new AuthorizationDecision(false);
    }

    /**
     * 返回授权器实例, 授权任何通过认证的访问
     *
     * @param <T> 检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     */
    public static <T> AuthorizationManager<T> authenticated() {
        return authenticated(null);
    }

    /**
     * 返回授权器实例, 授权任何通过认证的访问
     *
     * @param authenticationTrustResolver {@link AuthenticationTrustResolver} 实例
     * @param <T>                         检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
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

    /**
     * 返回授权器实例, 授权任何通过完整认证的访问
     *
     * @param <T> 检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
    public static <T> AuthorizationManager<T> fullyAuthenticated() {
        return fullyAuthenticated(null);
    }

    /**
     * 返回授权器实例, 授权任何通过完整认证的访问
     *
     * @param authenticationTrustResolver {@link AuthenticationTrustResolver} 实例
     * @param <T>                         检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
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

    /**
     * 返回授权器实例, 授权任何通过匿名的访问
     *
     * @param <T> 检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
    public static <T> AuthorizationManager<T> anonymous() {
        return anonymous(null);
    }

    /**
     * 返回授权器实例, 授权任何通过匿名的访问
     *
     * @param authenticationTrustResolver {@link AuthenticationTrustResolver} 实例
     * @param <T>                         检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
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

    /**
     * 返回授权器实例, 授权任何通过记住我的访问
     *
     * @param <T> 检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
    public static <T> AuthorizationManager<T> rememberMe() {
        return rememberMe(null);
    }

    /**
     * 返回授权器实例, 授权任何通过记住我的访问
     *
     * @param authenticationTrustResolver {@link AuthenticationTrustResolver} 实例
     * @param <T>                         检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see AuthenticationTrustResolver
     */
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

    /**
     * 返回授权器实例, 授权任何含有指定角色的访问
     *
     * @param role 角色名称
     * @param <T>  检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see RoleHierarchy
     */
    public static <T> AuthorizationManager<T> hasRole(String role) {
        return hasRole(null, role);
    }

    /**
     * 返回授权器实例, 授权任何含有指定角色的访问
     *
     * @param roleHierarchy {@link RoleHierarchy} 实例
     * @param role          角色名称
     * @param <T>           检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see RoleHierarchy
     */
    public static <T> AuthorizationManager<T> hasRole(@Nullable RoleHierarchy roleHierarchy, String role) {
        roleHierarchy = enforceRoleHierarchy(roleHierarchy);
        final var manager = AuthorityAuthorizationManager.<T>hasRole(role);
        manager.setRoleHierarchy(roleHierarchy);
        return manager;
    }

    /**
     * 返回授权器实例, 授权任何含有指定权限的访问
     *
     * @param authority 权限实例
     * @param <T>       检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     */
    public static <T> AuthorizationManager<T> hasAuthority(String authority) {
        return AuthorityAuthorizationManager.hasAuthority(authority);
    }

    /**
     * 返回授权器实例, 授权任何含有指定角色的访问
     *
     * @param roles 角色
     * @param <T>   检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see RoleHierarchy
     */
    public static <T> AuthorizationManager<T> hasAnyRole(String... roles) {
        return hasAnyRole(null, roles);
    }

    /**
     * 返回授权器实例, 授权任何含有指定角色的访问
     *
     * @param roleHierarchy {@link RoleHierarchy} 实例
     * @param roles         角色
     * @param <T>           检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see RoleHierarchy
     */
    public static <T> AuthorizationManager<T> hasAnyRole(@Nullable RoleHierarchy roleHierarchy, String... roles) {
        roleHierarchy = enforceRoleHierarchy(roleHierarchy);
        final var manager = AuthorityAuthorizationManager.<T>hasAnyRole(roles);
        manager.setRoleHierarchy(roleHierarchy);
        return manager;
    }

    /**
     * 返回授权器实例, 授权任何含有指定权限的访问
     *
     * @param authorities 角色
     * @param <T>         检查对象泛型
     * @return 授权器实例
     * @see HttpServletRequest
     * @see RequestAuthorizationContext
     * @see RoleHierarchy
     */
    public static <T> AuthorizationManager<T> hasAnyAuthority(String... authorities) {
        return AuthorityAuthorizationManager.hasAnyAuthority(authorities);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static AuthenticationTrustResolver enforceAuthenticationTrustResolver(@Nullable AuthenticationTrustResolver resolver) {
        return Objects.requireNonNullElse(resolver, DEFAULT_AUTHENTICATION_TRUST_RESOLVER);
    }

    private static RoleHierarchy enforceRoleHierarchy(@Nullable RoleHierarchy roleHierarchy) {
        return Objects.requireNonNullElse(roleHierarchy, DEFAULT_ROLE_HIERARCHY);
    }

}
