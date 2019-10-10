package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取参数
        String currentPageStr = request.getParameter("currentPage"); // 当前页码
        String pageSizeStr = request.getParameter("pageSize"); // 每页显示条数
        String cidStr = request.getParameter("cid"); // cid
        // 参数初始化
        int currentPage = 0;
        int pageSize = 0;
        int cid = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr); // 将string类型转为int
        }else {
            // 若客户端未传递参数，当前页码默认为1
            currentPage = 1;
        }
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            // 若客户端未传递参数，每页默认显示5条
            pageSize = 5;
        }
        if (cidStr != null && cidStr.length() > 0) {
            cid = Integer.parseInt(cidStr);
        }
        // 调用service查询PageBean对象
        RouteService service = new RouteServiceImpl();
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize);
        // 将PageBean对象序列化为json并发送到客户端
        writeValue(pb, response);
    }

}
