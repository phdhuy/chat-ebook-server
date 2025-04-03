package com.chatebook.security.exception;

import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.payload.response.ErrorResponse;
import com.chatebook.common.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Objects;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e)
            throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.UNAUTHORIZED);
        ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
        LogUtils.error(
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURL().toString(),
                e.getMessage());
        httpServletResponse
                .getWriter()
                .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    }
}
