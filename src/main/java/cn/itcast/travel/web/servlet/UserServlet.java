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

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    // 创建userService对象
    UserService service = new UserServiceImpl();

    /**
     * 用户注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 验证码校验
        // 获取客户端输入验证码
        String check = request.getParameter("check");
        // 获取服务器产生的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        // 判断
        if (!checkcode_server.equalsIgnoreCase(check)) {
            System.out.println("验证码错误");
            // 验证码有误
            // 创建错误信息对象
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码输入有误！");
            // 将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            // 将json数据写回客户端
            // 设置content-type
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return; // 验证码有误,后续操作不再执行
        }
        // 设置编码
        request.setCharacterEncoding("utf-8");
        // 获取接收数据
        Map<String, String[]> map = request.getParameterMap();
        // 封装为user对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        // 创建结果信息对象
        ResultInfo info = new ResultInfo();
        if (flag) {
            // 注册成功
            info.setFlag(true);
            System.out.println("注册成功！");
        }
        else {
            // 注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
            System.out.println("注册失败");
        }
        // 将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        // 将json数据写回客户端
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        //UserService service = new UserServiceImpl();
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

    /**
     * 查询一个用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从session中获取user对象
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.getUsername());
        // 将user对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        // 将json字符串写回客户端
        response.getWriter().write(json);
    }

    /**
     * 用户激活
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户的激活码
        String code = request.getParameter("code");
        if (code != null) {
            // 调用service，进行激活，根据激活码锁定唯一user对象,然后修改其status为Y
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);
            // 判断是否激活成功
            String msg = null;
            if (flag) {
                // 激活成功,跳转页面,此处简化
                System.out.println("激活成功！");
                msg = "激活成功！请<a href='login.html'>登录</a>";
            }
            else {
                // 激活失败
                System.out.println("激活失败！");
                msg = "激活失败！请联系管理员";
            }
            // 向客户端输出对应激活信息
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 销毁session
        request.getSession().invalidate();
        // 跳转到登录页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
