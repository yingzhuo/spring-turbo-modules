package spring.turbo.module.security.misc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author 应卓
 * @since 3.4.0
 */
public class GrantedAuthorityConverter implements Converter<String, GrantedAuthority> {

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public GrantedAuthority convert(String source) {
        return new SimpleGrantedAuthority(source);
    }

}
