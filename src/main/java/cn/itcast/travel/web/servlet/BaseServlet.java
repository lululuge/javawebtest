package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 利用baseservlet来进行同一类servlet的封装，以优化servlet代码
 *  *  无需被访问到，所有不用设置注解配置
 */

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("userServlet中的regist方法被执行了");
        // 完成方法分发
        // 1.获取uri路径
        String uri = req.getRequestURI();
        // 2.获取被调用的方法名
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);
        // 3.获取方法对象Method
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class); // this即userServlet
            // 4.执行方法
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将对象序列化为 json，并发送到客户端
     * @param obj
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), obj);
    }

    /**
     * 将对象序列化为json字符串
     * @param obj
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    public String writeValueAsString(Object obj, HttpServletResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        String json = mapper.writeValueAsString(obj);
        return json;
    }
}
