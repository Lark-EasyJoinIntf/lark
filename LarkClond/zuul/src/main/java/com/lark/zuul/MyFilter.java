package com.lark.zuul;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.micrometer.core.instrument.util.IOUtils;
import io.micrometer.core.ipc.http.HttpSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 该类在zuul路由的基础上，做了安全性的验证，本例是token参数，如果要看路由效果，加上参数或去掉@Component
 */
@Component
public class MyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);

    /**
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     pre：路由之前
     routing：路由之时
     post： 路由之后
     error：发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filterOrder：过滤的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        /*log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

            return null;
        }
        log.info("ok");
        return null;*/

        String token = null;
        String method = request.getMethod();
        if(hasBody(method)){
            if (!ctx.isChunkedRequestBody()) {
                ServletInputStream inp = null;
                try {
                    inp = request.getInputStream();
                    String body = null;
                    if (inp != null) {
                        body = IOUtils.toString(inp);
                    }
                    if(body != null){
                        JSONObject params = JSONObject.parseObject(body);
                        if(params.containsKey("token") && isValid(params.getString("token"))){
                            token = params.getString("token");
                        }else{
                            return false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }else{
            token = request.getParameter("token");
        }
        //解码token，验证是否登录及存在
        if(isValid(token)){
            JSONObject user = JWTTokenUtil.getInstance().getUserInfo(token);

        }else{
            return false;
        }
        /*System.err.println("REQUEST:: " + request.getScheme() + " " + request.getRemoteAddr() + ":" + request.getRemotePort());
        System.err.println("REQUEST:: " + request.getScheme() + " " + request.getRemoteAddr() + ":" + request.getRemotePort());
        StringBuilder params = new StringBuilder("?");
        Enumeration<String> names = request.getParameterNames();
        if( request.getMethod().equals("GET") ) {
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                params.append(name);
                params.append("=");
                params.append(request.getParameter(name));
                params.append("&");
            }
        }

        if (params.length() > 0) {
            params.delete(params.length()-1, params.length());
        }
        System.err.println("REQUEST:: > " + request.getMethod() + " " + request.getRequestURI() + params + " " + request.getProtocol());

        Enumeration<String> headers = request.getHeaderNames();

        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            String value = request.getHeader(name);
            System.err.println("REQUEST:: > " + name + ":" + value);
        }

        if (!ctx.isChunkedRequestBody()) {
            ServletInputStream inp = null;
            try {
                inp = request.getInputStream();
                String body = null;
                if (inp != null) {
                    body = IOUtils.toString(inp);
                    System.err.println("REQUEST:: > " + body);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
        return true;

    }

    private boolean hasBody(String method) {
        //只记录这3种谓词的body
        if (HttpSender.Method.POST.equals(method) || HttpSender.Method.PUT.equals(method))
            return true;
        return false;
    }

    private boolean isValid(String val){
        return val != null && val.length()>0;
    }
}
