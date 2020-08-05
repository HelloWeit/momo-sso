package cn.weit.sso.inner;

import cn.weit.sso.dto.UserDto;
import cn.weit.sso.exception.LogicException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author weitong
 */
@Component
public class MoUserDetailServiceImpl implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(MoUserDetailServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.equals("demo", username)) {
            throw new LogicException("用户名或密码不正确");
        }
        UserDto userDto = UserDto.gen();
        return new MoUserDetails(userDto, null);
    }

}
