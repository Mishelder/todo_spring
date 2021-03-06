package com.miaskor.todo_spring.filter;

import com.miaskor.todo_spring.model.Client;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

import static com.miaskor.todo_spring.util.URLs.LOGIN;
import static com.miaskor.todo_spring.util.URLs.getAllPublicURL;

@Component
public class MainFilter extends HttpFilter {

    private static final Set<String> PUBLIC_PAGES = getAllPublicURL();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var client = (Client) req.getSession().getAttribute("client");
        var uri = req.getRequestURI();
        if(uri.contains("about"))
            req.getSession().setAttribute("about",true);
        else
            req.getSession().removeAttribute("about");

        if (PUBLIC_PAGES.stream().anyMatch(uri::startsWith)) {
            chain.doFilter(req, res);
        }else if(Objects.nonNull(client)){
            chain.doFilter(req, res);
        } else {
            res.sendRedirect(req.getHeader("referer") == null
                    ? LOGIN :req.getHeader("referer"));
        }
    }
}
