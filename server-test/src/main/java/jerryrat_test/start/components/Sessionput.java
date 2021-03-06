package jerryrat_test.start.components;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;
import fun.jexing.container.HttpSession;

import java.util.Set;

/**
 * session测试
 */
@HttpComponent(url = "/put")
public class Sessionput implements Component {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        Set<String> parameterNames = request.getParameterNames();
        for (String parameterName : parameterNames) {
            System.out.println(request.getParameter(parameterName));
        }
        HttpSession session = request.getSession();
        //设置session
        session.set("hello","world");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.setMsg("session设置成功");
        response.finishResponse();
    }
}
