package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    private enum methods {GET, POST, DELETE}

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        new Thread(() -> controller = context.getBean(PostController.class)).start();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();


            if (path.equals("/api/posts")) {
                if (method.equals(methods.GET.toString())) {
                    controller.all(resp);
                    return;
                } else if (method.equals(methods.POST.toString())) {
                    controller.save(req.getReader(), resp);
                    return;
                }
            }
            if (path.matches("/api/posts/\\d+")) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                if (method.equals(methods.GET.toString())) {
                    controller.getById(id, resp);
                    return;
                } else if (method.equals(methods.DELETE.toString())) {
                    controller.removeById(id, resp);
                    return;
                }
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}