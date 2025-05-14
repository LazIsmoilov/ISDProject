package uts.isd.controller;

import uts.isd.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // 拿到或初始化购物车
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 从请求里读商品数据
        int productId    = Integer.parseInt(req.getParameter("productId"));
        int quantity     = Integer.parseInt(req.getParameter("quantity"));
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));

        // 加入购物车
        cart.add(new CartItem(productId, quantity, unitPrice));
        session.setAttribute("cart", cart);

        // 重定向回购物车或下单页
        resp.sendRedirect("createOrder.jsp");
    }
}
