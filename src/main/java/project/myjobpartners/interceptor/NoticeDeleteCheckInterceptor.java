package project.myjobpartners.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NoticeDeleteCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String dtype = (String) session.getAttribute("dtype");

        if (dtype.equals("GUEST") || dtype == null || session == null) {
            response.sendRedirect("/notice");
            return false;
        }

        return true;
    }
}
