<!DOCTYPE HTML>
<html>

    <head>
        <title>Offline Finger Matching</title>
        <meta name="description" content="website description" />
        <meta name="keywords" content="website keywords, website keywords" />
        <meta http-equiv="content-type" content="text/html; charset=windows-1252" />
        <link rel="stylesheet" type="text/css" href="style/style.css" title="style" />
        
      <script type="text/javascript">
        function showImage(){
            
            document.getElementById('gifImage').style.visibility='visible';
        }

      </script>
      <%!
        public boolean custStatus(){
        boolean cStatus = false;
        

        return cStatus;
        }
      %>
        
    </head>

    <body>
        
        <!--Change by rafsan-->
  <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //HTTP 1.1
            response.setHeader("Pragma", "no-cache");// HTTP 1.0
            response.setHeader("Expires", "0"); // proxies
            
            if(session.getAttribute("username")==null){
                response.sendRedirect("index.jsp");
            }
  %>
        <div id="main">
            <div id="header">
                <div id="logo">
                    <div id="logo_text">
                        <!-- class="logo_colour", allows you to change the colour of the text -->
                        <h1><a href="home.jsp">OFFLINE<span style="color: red">FINGER MATCHING</span></a></h1>
                    </div>
                </div>
                <div id="menubar">
                    <ul id="menu">
                        <!-- put class="selected" in the li tag for the selected page - to highlight which page you're on -->
                        <li><a href="home.jsp">Home</a></li>
                        <li class="selected"><a href="Finger_Matching.jsp">Finger Matching</a></li>
                        <li><a href="DatabaseInfo.jsp">Database Information</a></li>
                        <li><a href="/FingerOfflineMatch_1/logout">Sign Out</a></li>
                    </ul>
                </div>
            </div>
            <div id="site_content">

                <div id="content" style="width:100%" >
                    <!-- Agent Type -->
                    <%
                        String error_msg = (String) request.getParameter("eMessage");
                        if (error_msg != null) {%>
                            <div class="alert alert-danger">
                                <%=error_msg%> 
                            </div>
                            <%
                            }
                    %>
                    <div  class="column" style="height: 260px; padding: 5px; width: 30%; float: left;">
                            <img id="gifImage" src="style/ezgif.com-gif-maker.gif" alt="searchingData" style="width:100%;height: 100%; visibility: hidden;">
                        </div>
                    <form id="form1" action="/FingerOfflineMatch_1/FingerCheck">
                        
                        
                        
                        <div style = "background-color: white; height: 260px; padding: 10px; width: 30%; float: left; margin-left: 10px;">

                            <h4 style="text-align: center">Select Customer Type </h4><br><br>

                            <input type="text" name="agentPoint" id="agentPoint" placeholder="Specific Point No" onchange="if(this.checked){goto('/FingerOfflineMatch_1/FingerCheck')}">
                            <br><br>
                            <input type="text" name="custNo"  id="custNo" placeholder="Specific Cust Code"><br><br>
                            
                            <label>All Customer<label>
                                    <input type="checkbox" name="allCust" id="allCust"  value="all"><br><br>
                        </div>

                                    <!-- Outlet -->
                                    <div style="background-color: white; padding: 10px; height: 260px;width: 30%;  float: left;  margin-left: 10px; ">

                                        <h4 style="text-align: center">Select User Type </h4><br><br>
                                        <br>

                                        <input type="radio" name="userType" value="agent" required="required"> Agent<br>
                                        <input type="radio" name="userType" value="aro" required="required"> ARO<br>
                                        <input type="radio" name="userType" value="all" required="required"> All<br> 
                                        <br><br>
                                        <!--<input type="submit" value="Start Matching" onsubmit="showImage()" style="margin-bottom: 10px; color: white;cursor: pointer; background-color: green;  ">-->
                                        <button type="submit" id="Matching" value="Start Matching" form="form1" onclick="showImage()"  style="margin-bottom: 10px; cursor: pointer; color: white; background-color: green; text-align: center"/>Start Matching</button>
                                    </div>
                    </form>

                                    </div>
                                    </div>
                                    <div id="footer">
                                        Copyright &copy; <a href="http://validator.w3.org/check?uri=referer">Era InfoTech Ltd.</a>
                                    </div>
        </div>
    </body>
</html>
