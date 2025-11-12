package com.xaxc.teqin.component;

import com.xaxc.teqin.common.error.CommonErrorEnum;
import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String PARAM_TOKEN = "x-auth-token";

    public static final String TOKEN_CLAIM = "customClaim";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.debug("request uri: {}", uri);
        // The options method is passed directly.
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }
        String token = request.getHeader(PARAM_TOKEN);

        if (!StringUtils.hasText(token)) {
            token = request.getParameter(PARAM_TOKEN);
        }
        log.debug("token: {}", token);
        // Check if the token exists.
        if (!StringUtils.hasText(token)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":401,\"message\": \"token is null.\"}");
            log.error(CommonErrorEnum.NO_TOKEN.getErrorMsg());
            return false;
        }

        // Check if the current token is valid.
        try {
            Optional<CustomClaim> customClaimOpt = JwtUtil.parseToken(token);
            if (customClaimOpt.isEmpty()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":401,\"message\": \"token invalid.\"}");
                return false;
            }
            // Put the custom data from the token into the request.
            request.setAttribute(TOKEN_CLAIM, customClaimOpt.get());
            request.getSession().setAttribute(PARAM_TOKEN, token);
            return true;
        } catch (Exception e) {
            log.error("parseToken error:  {}", e.getMessage());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":401,\"message\": \"token invalid.\"}");
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Delete the custom data in the request after the request ends.
        request.removeAttribute(TOKEN_CLAIM);
    }
}
