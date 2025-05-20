<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 30/4/2025
  Time: 4:02 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<script>
    class Header extends HTMLElement {
        connectedCallback() {
            const shadow = this.attachShadow({ mode: "open" });
            shadow.innerHTML = `
                <style>
                    .header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        background-color: #333;
                        padding: 10px;
                        color: white;
                    }
                    .items {
                        list-style: none;
                        padding: 0;
                        display: flex;
                        gap: 15px;
                    }
                    .items a {
                        text-decoration: none;
                        color: white;
                        padding: 10px;
                        font-weight: bold;
                    }
                    .user-box {
                        display: flex;
                        gap: 10px;
                    }
                    .header-button {
                        background-color: transparent;
                        color: white;
                        font-weight: bold;
                        border: 2px solid white;
                        padding: 10px 30px;
                        cursor: pointer;
                        transition: 0.3s;
                    }
                    .header-button:hover {
                        background-color: white;
                        color: black;
                        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
                    }
                </style>
                <div class="header">
                    <ul class="items">
                        <li><a href="index.jsp">HOME</a></li>
                        <li><a href="register.jsp">REGISTER</a></li>
                        <li><a href="order?action=list">🧾 MY ORDERS</a></li>
                        <li><a href="#">ABOUT</a></li>
                        <li><a href="#">CONTACT</a></li>
                        <li><a href="main.jsp">MAIN</a></li>
                    </ul>
                    <div>
                        <h2>Logged in user:
                            <%
                                User loggedInUser = (User) session.getAttribute("user");
                                if (loggedInUser != null) {
                                    out.print(loggedInUser.getFullName());
                                } else {
                                    out.print("Guest");
                                }
                            %>
                        </h2>
                    </div>
                    <% if (session.getAttribute("user") != null) { %>
                    <div class="user-box">
                        <a href="profile-main.jsp"><button class="header-button">My Info</button></a>
                        <a href="logout.jsp"><button class="header-button">LOGOUT</button></a>
                    </div>
                    <% } %>
                </div>
            `;
        }
    }
    customElements.define("pref-header", Header);
</script>
