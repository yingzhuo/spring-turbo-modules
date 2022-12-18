/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authorization;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import spring.turbo.util.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @see #getInstance(HandlerMappingIntrospector)
 * @since 2.0.5
 */
public final class CompositeAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final AuthorizationDecision PERMIT = new AuthorizationDecision(true);
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);

    private final List<Mapping> mappings = new ArrayList<>();
    private final HandlerMappingIntrospector handlerMappingIntrospector;
    private RoleHierarchy roleHierarchy = new NullRoleHierarchy();
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private String rolePrefix = "ROLE_";

    public static CompositeAuthorizationManager getInstance(HandlerMappingIntrospector handlerMappingIntrospector) {
        return new CompositeAuthorizationManager(handlerMappingIntrospector);
    }

    private CompositeAuthorizationManager(HandlerMappingIntrospector handlerMappingIntrospector) {
        Asserts.notNull(handlerMappingIntrospector);
        this.handlerMappingIntrospector = handlerMappingIntrospector;
    }

    public CompositeAuthorizationManager addMapping(Mapping mapping) {
        mappings.add(mapping);
        return this;
    }

    public CompositeAuthorizationManager add(RequestMatcher matcher, AuthorizationManager<RequestAuthorizationContext> manager) {
        Asserts.notNull(matcher);
        Asserts.notNull(manager);

        mappings.add(
                new Mapping(
                        matcher,
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager permitAll(String... patterns) {
        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        (authentication, object) -> PERMIT
                )
        );
        return this;
    }

    public CompositeAuthorizationManager permitAll(HttpMethod method, String... patterns) {
        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        (authentication, object) -> PERMIT
                )
        );
        return this;
    }

    public CompositeAuthorizationManager denyAll(String... patterns) {
        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        (authentication, object) -> DENY
                )
        );
        return this;
    }

    public CompositeAuthorizationManager denyAll(HttpMethod method, String... patterns) {
        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        (authentication, object) -> DENY
                )
        );
        return this;
    }

    public CompositeAuthorizationManager hasRole(String role, String patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAnyRole(this.rolePrefix, new String[]{role});
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager hasRole(String role, HttpMethod method, String patterns) {

        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAnyRole(this.rolePrefix, new String[]{role});
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager hasAnyRole(List<String> roles, String patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAnyRole(this.rolePrefix, roles.toArray(new String[0]));
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );

        return this;
    }

    public CompositeAuthorizationManager hasAnyRole(List<String> roles, HttpMethod method, String patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAnyRole(this.rolePrefix, roles.toArray(new String[0]));
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );

        return this;
    }

    public CompositeAuthorizationManager hasAuthority(String authority, String... patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAuthority(authority);
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager hasAuthority(String authority, HttpMethod method, String... patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAuthority(authority);
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager hasAnyAuthority(List<String> authorities, String... patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAnyAuthority(authorities.toArray(new String[0]));
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager hasAnyAuthority(List<String> authorities, HttpMethod method, String... patterns) {
        var manager =
                AuthorityAuthorizationManager.<RequestAuthorizationContext>hasAnyAuthority(authorities.toArray(new String[0]));
        manager.setRoleHierarchy(roleHierarchy);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager authenticated(String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>authenticated();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager authenticated(HttpMethod method, String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>authenticated();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager fullyAuthenticated(String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>fullyAuthenticated();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager fullyAuthenticated(HttpMethod method, String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>fullyAuthenticated();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager rememberMe(String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>rememberMe();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager rememberMe(HttpMethod method, String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>rememberMe();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager anonymous(String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>anonymous();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager anonymous(HttpMethod method, String... patterns) {
        var manager = AuthenticatedAuthorizationManager.<RequestAuthorizationContext>anonymous();
        manager.setTrustResolver(authenticationTrustResolver);

        this.mappings.add(
                new Mapping(
                        patternsToMatcher(method, patterns),
                        manager
                )
        );
        return this;
    }

    public CompositeAuthorizationManager setRoleHierarchy(RoleHierarchy roleHierarchy) {
        Asserts.notNull(roleHierarchy);
        this.roleHierarchy = roleHierarchy;
        return this;
    }

    public CompositeAuthorizationManager setRoleHierarchyStringRepresentation(String roleHierarchyStringRepresentation) {
        Asserts.hasText(roleHierarchyStringRepresentation);
        var rh = new RoleHierarchyImpl();
        rh.setHierarchy(roleHierarchyStringRepresentation);
        this.roleHierarchy = rh;
        return this;
    }

    public CompositeAuthorizationManager setAuthenticationTrustResolver(AuthenticationTrustResolver authenticationTrustResolver) {
        Asserts.notNull(authenticationTrustResolver);
        this.authenticationTrustResolver = authenticationTrustResolver;
        return this;
    }

    public CompositeAuthorizationManager setRolePrefix(String rolePrefix) {
        Asserts.hasText(rolePrefix);
        this.rolePrefix = rolePrefix;
        return this;
    }

    private RequestMatcher patternsToMatcher(String... patterns) {
        Asserts.notNull(patterns);
        Asserts.noNullElements(patterns);
        Asserts.isTrue(patterns.length > 0);

        final var list = new ArrayList<RequestMatcher>(patterns.length);
        for (var pattern : patterns) {
            list.add(new MvcRequestMatcher.Builder(this.handlerMappingIntrospector).pattern(pattern));
        }
        return new OrRequestMatcher(list);
    }

    private RequestMatcher patternsToMatcher(HttpMethod method, String... patterns) {
        Asserts.notNull(method);
        Asserts.notNull(patterns);
        Asserts.noNullElements(patterns);
        Asserts.isTrue(patterns.length > 0);

        final var list = new ArrayList<RequestMatcher>(patterns.length);
        for (var pattern : patterns) {
            list.add(new MvcRequestMatcher.Builder(this.handlerMappingIntrospector).pattern(method, pattern));
        }
        return new OrRequestMatcher(list);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        if (mappings.isEmpty()) {
            return DENY;
        }
        for (final var mapping : mappings) {
            final var matcher = mapping.matcher;
            final var manager = mapping.manager;
            final var request = object.getRequest();

            if (matcher.matches(request)) {
                return manager.check(authentication, object);
            }
        }
        return DENY;
    }

    public static record Mapping(RequestMatcher matcher, AuthorizationManager<RequestAuthorizationContext> manager) {
        public Mapping {
            Asserts.notNull(manager);
            Asserts.notNull(manager);
        }
    }

}
