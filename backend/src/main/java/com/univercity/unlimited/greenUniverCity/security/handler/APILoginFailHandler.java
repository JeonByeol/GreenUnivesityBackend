package com.univercity.unlimited.greenUniverCity.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@Slf4j
public class APILoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
      log.info("Login 실패.....",exception);
        Gson gson= new Gson();
        String jsonStr =gson.toJson(Map.of("error","ERROR_LOGIN"));
        response.setContentType("application/json");
        PrintWriter printWriter= response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}
