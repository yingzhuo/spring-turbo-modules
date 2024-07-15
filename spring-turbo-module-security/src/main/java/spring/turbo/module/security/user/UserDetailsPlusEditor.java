package spring.turbo.module.security.user;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import spring.turbo.databinding.AbstractPropertyEditor;
import spring.turbo.exception.DataBindingException;
import spring.turbo.util.StringPool;
import spring.turbo.util.text.StringMatcher;
import spring.turbo.util.text.TextVariables;

import java.time.LocalDate;

import static spring.turbo.util.CharPool.LF;
import static spring.turbo.util.CharPool.SEMICOLON;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class UserDetailsPlusEditor extends AbstractPropertyEditor<UserDetailsPlus> {

    public static final String AUTHORITIES = "authorities";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ENABLED = "enabled";
    public static final String ACCOUNT_NON_EXPIRED = "accountNonExpired";
    public static final String ACCOUNT_NON_LOCKED = "accountNonLocked";
    public static final String CREDENTIALS_NON_EXPIRED = "credentialsNonExpired";
    public static final String ID = "id";
    public static final String AVATAR = "avatar";
    public static final String EMAIL = "mail";
    public static final String PHONE_NUM = "phoneNumber";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String BIOGRAPHY = "biography";

    @Override
    protected UserDetailsPlus convert(String text) throws DataBindingException {
        var variables = new TextVariables(text, StringMatcher.charSetMatcher(SEMICOLON, LF));
        variables.put("createdTime", String.valueOf(System.currentTimeMillis()));

        var builder = UserDetailsPlus.builder();

        // 角色/权限
        var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(variables.getVariableValue(AUTHORITIES));
        if (!CollectionUtils.isEmpty(authorities)) {
            builder.authorities(authorities);
        }

        // 用户名
        var username = variables.getVariableValue(USERNAME);
        if (StringUtils.hasText(username)) {
            builder.username(username);
        }

        // 密码
        var password = variables.getVariableValue(PASSWORD, StringPool.EMPTY);
        if (StringUtils.hasText(password)) {
            builder.password(password);
        }

        // accountNonExpired
        try {
            var accountNonExpired = variables.getVariableValue(ACCOUNT_NON_EXPIRED, "true");
            builder.accountExpired(!Boolean.parseBoolean(accountNonExpired));
        } catch (Exception e) {
            builder.accountExpired(false);
        }

        // accountNonLocked
        try {
            var accountNonLocked = variables.getVariableValue(ACCOUNT_NON_LOCKED, "true");
            builder.accountExpired(!Boolean.parseBoolean(accountNonLocked));
        } catch (Exception e) {
            builder.accountLocked(false);
        }

        // credentialsNonExpired
        try {
            var credentialsNonExpired = variables.getVariableValue(CREDENTIALS_NON_EXPIRED, "true");
            builder.credentialsExpired(!Boolean.parseBoolean(credentialsNonExpired));
        } catch (Exception e) {
            builder.credentialsExpired(false);
        }

        // enabled
        try {
            var enabled = variables.getVariableValue(ENABLED, "true");
            builder.disabled(!Boolean.parseBoolean(enabled));
        } catch (Exception e) {
            builder.disabled(false);
        }

        // id
        var id = variables.getVariableValue(ID);
        if (StringUtils.hasText(id)) {
            builder.id(id);
        }

        // email
        var email = variables.getVariableValue(EMAIL);
        if (StringUtils.hasText(email)) {
            builder.email(email);
        }

        // avatar
        var avatar = variables.getVariableValue(AVATAR);
        if (StringUtils.hasText(avatar)) {
            builder.avatar(avatar);
        }

        // phone number
        var phoneNumber = variables.getVariableValue(PHONE_NUM);
        if (StringUtils.hasText(phoneNumber)) {
            builder.phoneNumber(phoneNumber);
        }

        // dob
        var dob = variables.getVariableValue(DATE_OF_BIRTH);
        if (StringUtils.hasText(dob)) {
            try {
                builder.dateOfBirth(LocalDate.parse(dob));
            } catch (Exception e) {
                builder.dateOfBirth(dob);
            }
        }

        // biography
        var bio = variables.getVariableValue(BIOGRAPHY);
        if (StringUtils.hasText(bio)) {
            builder.biography(bio);
        }

        return builder.build();
    }

}
