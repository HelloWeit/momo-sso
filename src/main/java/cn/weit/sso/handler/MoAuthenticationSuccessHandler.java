package cn.weit.sso.handler;

import cn.weit.sso.web.RestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


/**
 * @author weitong
 */
@Component("moAuthenticationSuccessHandler")
public class MoAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MoAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        LOG.info("登录成功！");
        Map<String, String> principalMap = (Map<String, String>) authentication.getPrincipal();
        HttpSession session = request.getSession();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        com.fasterxml.jackson.databind.ObjectMapper om = new ObjectMapper();
        out.write(om.writeValueAsString(RestResult.success(principalMap.get("ref"))));
        out.flush();
        out.close();
    }

}
