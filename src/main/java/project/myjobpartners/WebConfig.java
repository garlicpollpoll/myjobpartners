package project.myjobpartners;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.myjobpartners.interceptor.LoginCheckInterceptor;
import project.myjobpartners.interceptor.NoticeDeleteCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/inquiry_write");

        registry.addInterceptor(new NoticeDeleteCheckInterceptor())
                .order(2)
                .addPathPatterns("/notice_delete/**");
    }
}
