package com.kongwc.tools.adapter;

import com.kongwc.tools.security.ManageWebAuthenticationProvider;
import com.kongwc.tools.security.SmartLoginUrlAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by zhanghx on 2017/09/22.
 */
@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ManageWebAuthenticationProvider provider;//自定义验证
    @Autowired
    private UserDetailsService userDetailsService;//自定义用户服务
    @Autowired
    private SmartLoginUrlAuthenticationEntryPoint authenticationEntryPoint;// ajax访问时候认证失败处理

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(
                        "/**/*.js", "/**/*.css", "/**/*.img", "/**/*.png", "/**/*.jpg").permitAll()//无需访问权限

                .anyRequest()//all others request authentication
                .authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //将验证过程交给自定义验证工具
        AuthenticationManagerBuilder authenticationManagerBuilder = auth.authenticationProvider(provider);
    }
}
