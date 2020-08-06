package cn.weit.sso.inner;

import cn.weit.sso.exception.LogicException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author weitong
 */
@Component
public class MoAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    @Qualifier("moUserDetailServiceImpl")
    private UserDetailsService userDetailsService;

    private UserCache userCache = new NullUserCache();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Map<String, String> principalMap = (Map<String, String>) authentication.getPrincipal();
        String userName = principalMap.get("userName");
        UserDetails userDetails = userCache.getUserFromCache(userName);
        if (userDetails == null) {
            userDetails = userDetailsService.loadUserByUsername(userName);
        }
        String password = (String) authentication.getCredentials();
        if (!StringUtils.equals("123456", password)) {
            throw new LogicException("用户名或密码错误");
        }

        userCache.putUserInCache(userDetails);

        return new MoLoginAuthenticationToken(userName, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(MoLoginAuthenticationToken.class);
    }
}
