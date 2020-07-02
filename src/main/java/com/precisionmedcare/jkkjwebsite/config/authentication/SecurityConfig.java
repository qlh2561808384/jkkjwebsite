package com.precisionmedcare.jkkjwebsite.config.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * extends 继承 WebSecurityConfigurerAdapter 类，并重写它的方法来设置一些web安全的细节。
 * 我们结合@EnableWebSecurity注解和继承WebSecurityConfigurerAdapter，来给我们的系统加上基于web的安全机制。
 */
@Configuration//加载到spring容器
//@EnableWebSecurity//Spring Security用于启用Web安全的注解  通过 @EnableWebSecurity注解开启Spring Security的功能
//@EnableGlobalMethodSecurity(prePostEnabled = true)//可以开启security的注解，我们可以在需要控制权限的方法上面使用@PreAuthorize，@PreFilter这些注解。
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyOncePerRequestFilter myOncePerRequestFilter;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    //退出处理器
    @Autowired
    private MyLogoutHandler myLogoutHandler;
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    private final String[] releasePath = new String[]{
            "/user/register"
            , "/swagger-ui.html"
            , "/v2/api-docs"
            , "/swagger-resources"
            , "/swagger-resources/**"
            , "/configuration/ui"
            , "/configuration/security"
            , "/swagger-ui.html/**"
            , "/webjars/**"
    };

    /**
     *  认证:
     *      登陆相关
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //======================================================================================================
        //第1步：解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求）
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        //第2步：让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl();

        //第3步：登录,因为使用前端发送JSON方式进行登录，所以登录模式不设置也是可以的。
        http.formLogin();

        //第4步：拦截账号、密码。覆盖 UsernamePasswordAuthenticationFilter过滤器
        http.addFilterAt(myUsernamePasswordAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);


        //======================================================================================================

        //第5步：拦截token，并检测。在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);


        //第6步：请求权限配置
        //放行注册API请求，其它任何请求都必须经过身份验证.
        http.authorizeRequests()
                /**
                 *  arr是一个数组，
                 *      如果放行api多的话可以存数据库 然后从数据库里面取出来
                 *      如果放行的api不多，直接写死就行。
                 *  .antMatchers(arr).permitAll()
                 */
//                .antMatchers("/user/register", "/user/getPasswordByEmail", "/user/reset_password", "/swagger-ui.html").permitAll()
                .antMatchers(releasePath).permitAll();
                //在web下的API，无需登录即可访问
                //nginx。。。。。。
                // .antMatchers("/web/**").permitAll()
                //ROLE_ADMIN可以操作任何事情
                //同等上一行代码
                // http://localhsot:8080/organization
//
//                .antMatchers("/admin/**","/organization","/product/**").hasAuthority("ROLE_A")
//
//                .antMatchers("/ccc/**").hasAnyAuthority("ROLE_A","ROLE_B")
//                .antMatchers("/aaaa").hasAuthority("ROLE_B")
//                .antMatchers("/bbb/**").hasAuthority("ROLE_B")
                /*
                 由于使用动态资源配置，以上代码在数据库中配置如下：
                 在sys_backend_api_table中添加一条记录
                 backend_api_id=1，
                 backend_api_name = 所有API，
                 backend_api_url=/**,
                 backend_api_method=GET,POST,PUT,DELETE
                 */

                //动态加载资源,
//                .anyRequest().access("@dynamicPermission.checkPermisstion(request,authentication)");

        //======================================================================================================

        //第7步：处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint).accessDeniedHandler(myAccessDeniedHandler);

        //第8步：退出
        http.logout().addLogoutHandler(myLogoutHandler).logoutSuccessHandler(myLogoutSuccessHandler);
    }

    /**
     * 手动注册账号、密码拦截器  首先执行的
     * @return
     * @throws Exception
     */
    @Bean
    MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        //失败后处理
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
