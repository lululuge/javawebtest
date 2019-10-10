package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    // 创建RouteDao对象
    private RouteDao dao = new RouteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
        // 创建PageBean对象
        PageBean<Route> pb = new PageBean<Route>();
        // 查询总记录数
        int totalCount = dao.findTotalCount(cid);
        // 计算总页数
        int totalPage;
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        }else {
            totalPage = totalCount / pageSize + 1;
        }
        // 计算当前页的开始索引
        int start = pageSize * (currentPage - 1);
        // 查询当前页的数据集合
        List<Route> routeList = dao.findByPage(cid, start, pageSize);
        // 封装对象
        pb.setTotalCount(totalCount);
        pb.setPageSize(pageSize);
        pb.setCurrentPage(currentPage);
        pb.setTotalPage(totalPage);
        pb.setList(routeList);
        return pb;
    }
}
