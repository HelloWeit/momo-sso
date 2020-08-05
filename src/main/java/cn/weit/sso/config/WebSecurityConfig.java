package cn.weit.sso.config;

import net.qihoo.qilin.common.sso.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;


    public LoginFilterUtil customLoginFilter() throws Exception {
        LoginFilterUtil filter = new LoginFilterUtil(loginType, redisTemplate, loginExpire);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/**"));
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerUtil());
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerUtil());
        return filter;
    }

    /**
     * HttpSecurity（HTTP请求安全处理）
     * 具体的权限控制规则配置。一个这个配置相当于xml配置中的一个标签。
     * 各种具体的认证机制的相关配置，OpenIDLoginConfigurer、AnonymousConfigurer、FormLoginConfigurer、HttpBasicConfigurer等。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用CSRF保护
        //CSRF在SpringSecurity中默认是启动的，那么你的退出请求必须改为POST请求。这确保了注销需要CSRF令牌和一个恶意的用户不能强制注销用户
        http.csrf().disable();
        http
                .exceptionHandling()
                //未登录时输出json报错,不进行跳转
                .accessDeniedHandler(new AccessDeniedHandlerUtil())
                .authenticationEntryPoint(new AuthenticationEntryPointUtil())
                .and()
            .authorizeRequests()
                //尚未匹配以oauth开头的任何URL都要求用户进行身份验证
                //.antMatchers("/**/login", "/**/login/gateway", "/**/logout")
                //.permitAll()
                //.antMatchers("/**").denyAll()
                .anyRequest()
                .authenticated()
                .and()
            .formLogin()
                //指定登录页的路径
                //表单登录
                .loginPage("/login/gateway")
                .loginProcessingUrl("/**/login")
                .successHandler(new AuthenticationSuccessHandlerUtil())
                .failureHandler(new AuthenticationFailureHandlerUtil())
                //必须允许所有用户访问我们的登录页（例如未验证的用户，否则验证流程就会进入死循环）
                //这个formLogin().permitAll()方法允许所有用户基于表单登录访问这个page路径。
                .permitAll()
                .and()
            .logout()
                //登出相关配置，这里配置了登出处理器
                .addLogoutHandler(new LogoutHandlerUtil())
                .and()
                //允许配置会话管理, 会话管理Filter，持久化用户登录信息，可以保存到session中，也可以保存到cookie或者redis中
                //不创建会话 - 即通过前端传token到后台过滤器中验证是否存在访问权限
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
             //允许多设备同时登录
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        ;
        //下面代码展示如何在过滤器链中插入自己的过滤器，addFilterBefore加在对应的过滤器之前addFilterAfter之后，
        // addFilterAt加在过滤器同一位置，事实上框架原有的Filter在启动HttpSecurity配置的过程中，
        // 都由框架完成了其一定程度上固定的配置，是不允许更改替换的。根据测试结果来看，
        // 调用addFilterAt方法插入的Filter，会在这个位置上的原有Filter之前执行。
        //http.addFilterBefore(new AdaptationFilter(), WebAsyncManagerIntegrationFilter.class);
        //UsernamePasswordAuthenticationFilter：用于处理基于表单的登录请求，从表单中获取用户名和密码。
        // 默认情况下处理来自“/login”的请求。从表单中获取用户名和密码时，
        // 默认使用的表单name值为“username”和“password”，
        // 这两个值可以通过设置这个过滤器的usernameParameter 和 passwordParameter 两个参数的值进行修改。
        // 自定义过滤器
        http.addFilterBefore(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * AuthenticationManagerBuilder（身份验证管理生成器）
     * 用来配置全局的认证相关的信息，其实就是AuthenticationProvider和UserDetailsService，前者是认证服务提供商，后者是用户详情查询服务；
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //并根据传入的AuthenticationManagerBuilder中的userDetailsService方法来接收我们自定义的认证方法。
        //且该方法必须要实现UserDetailsService这个接口。
        //auth.userDetailsService(new UserDetailsUtil());
                //密码使用BCryptPasswordEncoder()方法验证，因为这里使用了BCryptPasswordEncoder()方法验证。所以在注册用户的时候在接收前台明文密码之后也需要使用BCryptPasswordEncoder().encode(明文密码)方法加密密码。
        //       .passwordEncoder(new BCryptPasswordEncoder());;
        auth.authenticationProvider(new AuthenticationProviderUtil());
    }

    /**
     * WebSecurity（WEB安全）
     * 全局请求忽略规则配置（比如说静态文件，比如说注册页面）、
     * 全局HttpFirewall配置、是否debug配置、全局SecurityFilterChain配置、
     * privilegeEvaluator、expressionHandler、securityInterceptor；
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring()
                .antMatchers("/favicon.ico", "/**/assets/**",
                        "/**/v2/api-docs",
                        "/**/swagger-resources",
                        "/**/swagger-resources/**",
                        "/**/configuration/ui",
                        "/**/configuration/security",
                        "/**/swagger-ui.html/**",
                        "/**/webjars/**")
                .mvcMatchers("/**/login/gateway/**", "/**/logout", "/**/login/rewrite/**");
        super.configure(web);
    }
}
