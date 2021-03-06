package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
import fun.jexing.container.Context;
import fun.jexing.container.HttpSession;
import fun.jexing.utils.HeaderUtil;

import java.net.Socket;
import java.util.Set;

public class Request implements HttpRequest{
    private RequestStringParser parser;
    private Cookie[] cookies;
    private ServerConfig serverConfig;
    private Socket socket;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Cookie[] getCookie() {
        return cookies;
    }

    public RequestStringParser getParser() {
        return parser;
    }

    public void setParser(RequestStringParser parser) {
        this.parser = parser;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }


    @Override
    public boolean forward(String url,HttpRequest request,HttpResponse response) {
        if (url == null){
            return false;
        }
        //如果容器中存在则调用容器方法
        if (context.exist(url)){
            context.invoke(url,request,response);
        }else{
            //否则查找静态资源
            Response rootResponse = (Response)response;
            rootResponse.staticResourceResponse(url);
        }
        return true;
    }

    @Override
    public String getRequestBody() {
        return parser.getBody();
    }

    @Override
    public int getContentLength() {
        String s = parser.getHeaderMap().get(HeaderUtil.CONTENT_LENGTH_NAME);
        return s == null? -1 : Integer.parseInt(s);
    }

    @Override
    public String getContentType() {
        return parser.getHeaderMap().get(HeaderUtil.CONTENT_TYPE_NAME);
    }

    @Override
    public String getParameter(String var1) {
        return parser.getParameterMap().get(var1);
    }

    @Override
    public Set<String> getParameterNames() {
        return parser.getParameterMap().keySet();
    }

    @Override
    public String getProtocol() {
        return parser.getProtocol();
    }

    @Override
    public int getServerPort() {
        return serverConfig.getPort();
    }

    @Override
    public String getRemoteAddr() {
        return socket.getInetAddress().toString();
    }

    @Override
    public String getRemoteHost() {
        return socket.getInetAddress().toString();
    }

    @Override
    public int getRemotePort() {
        return socket.getPort();
    }

    @Override
    public String getMethod() {
        return parser.getMethod();
    }

    @Override
    public String getRequestURI() {
        return parser.getUrl();
    }

    @Override
    public String getHeader(String name) {
        return parser.getHeaderMap().get(name);
    }

    @Override
    public Set<String> getHeaderNames() {
        return parser.getHeaderMap().keySet();
    }

    @Override
    public HttpSession getSession() {
        Cookie cookie = getSessionIdCookie();
        if (cookie == null){
            return null;
        }
        return context.getSession(cookie.getValue());
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Cookie getSessionIdCookie(){
        if (cookies == null){
            return null;
        }
        for (Cookie c : cookies) {
            if (HttpProcessor.SESSIONID.equals(c.getName())){
                return c;
            }
        }
        return null;
    }
}
