package gg.jte.demo;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.Utf8ByteOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@Service
public class TemplateRenderer {
    private final TemplateEngine templateEngine;

    public TemplateRenderer() {
        CodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte"));
        templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        templateEngine.setBinaryStaticContent(true);
    }

    public void render(String name, Object model, HttpServletResponse response) {
        Utf8ByteOutput output = new Utf8ByteOutput();
        templateEngine.render(name, model, output);

        response.setContentType("text/html");
        response.setContentLength(output.getContentLength());

        try {
            output.writeTo(response.getOutputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
