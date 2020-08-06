package cn.weit.sso.inner;

import cn.weit.sso.exception.LogicException;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author weitong
 */
public class MoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(MoAuthenticationFilter.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = this.obtainUsername(request);
        if (StringUtils.isBlank(userName)) {
            throw new LogicException("用户名或密码有误");
        }
        String password = this.obtainPassword(request);
        if (StringUtils.isBlank(password)) {
            throw new LogicException("用户名或密码有误");
        }
        String ref = request.getParameter("ref");
        if (StringUtils.isBlank(ref)) {
            ref = "";
        }
        Map<String, String> principalMap = Maps.newHashMap();
        principalMap.put("principal", userName);
        principalMap.put("ref", ref);
        MoLoginAuthenticationToken moLoginAuthenticationToken = new MoLoginAuthenticationToken(principalMap, password);
        moLoginAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(moLoginAuthenticationToken);
    }


}
