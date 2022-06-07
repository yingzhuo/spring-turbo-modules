## spring-turbo-module-security

本模块是对[spring-security](https://spring.io/projects/spring-security)的扩展 (以下简称SS)。

### (1) 对PasswordEncoder进行扩展

请参考[PasswordEncoderFactories](./src/main/java/spring/turbo/module/security/encoder/PasswordEncoderFactories.java)
，对SS原生的内容做了若干扩展。

### (2) 更友好地支持以令牌为基础的无状态的认证与授权

#### 2.1 核心概念 - 令牌

令牌是对“附着”在一次HTTP请求上的一个用于表达用户的字符串的抽象。我们把它称为Token。

```java

@FunctionalInterface
public interface Token extends Serializable {

    public String asString();

}
```

#### 2.2 核心概念 - 令牌解析器

令牌解析器是一个核心部件，它(的实现)负责从一次HTTP中找到令牌。

```java

@FunctionalInterface
public interface TokenResolver extends Ordered {

    public Optional<Token> resolve(WebRequest request);

    @Override
    public default int getOrder() {
        return 0;
    }

}
```

如你看到的那样，解析器有可能无法解析出令牌。这时它将返回一个空的Optional实例。

#### 2.3 核心概念 - 令牌用户转换器

这依然是一个核心部件，他(的实现)从令牌信息获取用户信息。

```java

@FunctionalInterface
public interface TokenToUserConverter extends Converter<Token, UserDetails> {

    @Nullable
    @Override
    public UserDetails convert(@Nullable Token token) throws AuthenticationException;

    @Nullable
    public default UserDetails convert(String rawToken) throws AuthenticationException {
        return convert(StringToken.of(rawToken));
    }

}
```

#### 2.4 核心概念 - 认证Filter

熟悉SS的人一定知道，SS是通过HttpServletFilter实例完成请求的认证。正因为如此本模块提供一个Filter实现完成此功能。
请参考源代码[TokenAuthenticationFilter](src/main/java/spring/turbo/module/security/filter/TokenAuthenticationFilter.java)
