package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 校验验证码
        HttpSession session = request.getSession();
        // 获取服务器生成的验证码
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        // 获取客户端输入验证码
        String check = request.getParameter("check");
        // 判断
        if (!checkcode_server.equalsIgnoreCase(check)) {
            // 验证码输入有误
            // 创建结果信息对象
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码输入有误！");
            // 将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            // 设置content-type
            response.setContentType("application/json;charset=utf-8");
            // 将json字符串写回客户端
            response.getWriter().write(json);
            return;
        }
        // 设置编码
        request.setCharacterEncoding("utf-8");
        // 获取客户端数据,并封装为user对象
        Map<String, String[]> map = request.getParameterMap();
        User loginUser = new User();
        try {
            BeanUtils.populate(loginUser, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 调用service进行登录
        UserService service = new UserServiceImpl();
        User user = service.login(loginUser);
        // 判断user是否为null
        if (user != null) {
            // 若不为null，继续判断该user是否进行了激活
            if (user.getStatus().equals("Y")) {
                // 已激活，登录成功，跳转到主页index.html
                System.out.println("登录成功");
                // 将user对象存入session中
                request.getSession().setAttribute("user", user);
//                response.sendRedirect(request.getContextPath() + "/index.html"); // 动态获取虚拟目录
                // 创建结果信息对象
                ResultInfo info = new ResultInfo();
                info.setFlag(true);
                // 将info对象序列化为json
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(info);
                // 设置content-type
                response.setContentType("application/json;charset=utf-8");
                // 将json字符串写回客户端
                response.getWriter().write(json);
            }
            else {
                // 该用户未激活，登录失败，响应错误信息（该用户尚未激活，请激活后进行登录！）
                System.out.println("该用户尚未激活，请激活后进行登录！");
                // 创建结果信息对象
                ResultInfo info = new ResultInfo();
                info.setFlag(false);
                info.setErrorMsg("该用户尚未激活，请激活后进行登录！");
                // 将info对象序列化为json
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(info);
                // 设置content-type
                response.setContentType("application/json;charset=utf-8");
                // 将json字符串写回客户端
                response.getWriter().write(json);
            }
        }
        else {
            // 若为null，登录失败，响应错误信息(用户名或密码错误！)
            System.out.println("用户名或密码错误！");
            // 创建结果信息对象
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
            // 将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            // 设置content-type
            response.setContentType("application/json;charset=utf-8");
            // 将json字符串写回客户端
            response.getWriter().write(json);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
