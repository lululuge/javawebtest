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

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        UserService service = new UserServiceImpl();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
