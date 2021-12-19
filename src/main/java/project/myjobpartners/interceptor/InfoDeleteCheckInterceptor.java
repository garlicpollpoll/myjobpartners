package project.myjobpartners.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InfoDeleteCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String dtype = (String) session.getAttribute("dtype");

        if (session == null || dtype == null || dtype.equals("GUEST")) {
            response.sendRedirect("/info");
            return false;
        }

        return true;
    }
}
