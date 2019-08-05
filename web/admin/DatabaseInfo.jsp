<%@page import="era.com.propertiesFile.GetDBInfo"%>
<%@page import="era.com.propertiesFile.DBInfo"%>
<!DOCTYPE HTML>
<html>

<head>
  <title>Database Information</title>
  <meta name="description" content="website description" />
  <meta name="keywords" content="website keywords, website keywords" />
  <meta http-equiv="content-type" content="text/html; charset=windows-1252" />
  <link rel="stylesheet" type="text/css" href="style/style.css" title="style" />
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
          <h1><a href="home.jsp">OFFLINE<span style="color:red">FINGER MATCHING</span></a></h1>
          
        </div>
      </div>
      <div id="menubar">
        <ul id="menu">
          <!-- put class="selected" in the li tag for the selected page - to highlight which page you're on -->
          <li><a href="home.jsp">Home</a></li>
          <li><a href="Finger_Matching.jsp">Finger Matching</a></li>
          <li class="selected"><a href="DatabaseInfo.jsp">Database Information</a></li>
          <li><a href="/FingerOfflineMatch_1/logout">Sign Out</a></li>
        </ul>
      </div>
    </div>
    <div id="site_content">
      <div class="sidebar">
        <!-- insert your sidebar items here -->
        
      </div>
      <div id="content">
        <!-- insert the page content here -->
        <h1>Set Database Information Here</h1>
        <p>Below is an example of how a contact form might look with this template:</p>
        <%
            String contextPath = request.getContextPath();
        %>
        <form action="<%=contextPath%>/WriteDatabasePropertiesFile" role="form" method="post">
          <div class="form_settings">
            <p><span>IP</span><input class="contact" type="text" name="ip" value="" placeholder="127.10.10.72" required="required"/></p>
            <p><span>Port</span><input class="contact" type="text" name="portNo" value="" placeholder="8080" required="required"/></p>
            <p><span>Service Name</span><input class="contact" type="text" name="serviceName" value="" placeholder="service name" required="required"/></p>
            <p><span>User</span><input class="contact" type="text" name="userName" value="" placeholder="Email or Name" required="required"/></p>
            <p><span>Password</span><input class="contact" type="password" name="password" value="" placeholder="Password" required="required"/></p>
            <p style="padding-top: 15px"><span>&nbsp;</span><input class="submit" type="submit" name="contact_submitted" value="submit" /></p>
          </div>
        </form>
          <label style="color: red">Database Information</label> 
          <br/><br/>
         <% DBInfo d = GetDBInfo.getDbInfo();
         String IP =d.ip ;
         String PortNo =d.portNo ;
         String serviceName =d.serviceName ;
         String userName =d.userName ;
         String password =d.password ;
         %>
          
         <p><span>IP:</span><%=IP%> 
         <span>, Port No:</span><%=PortNo%>   
         <span>, Service Name:</span><%=serviceName%>   
         <span>, User:</span><%=userName%>   
         <span>, Password:</span><%=password%> </p>   
           
          
        <p><br /><br />NOTE: A contact form such as this would require some way of emailing the input to an email address.</p>
      </div>
    </div>
    <div id="footer">
      Copyright &copy; <a href="http://validator.w3.org/check?uri=referer">Era InfoTech Ltd.</a>
    </div>
  </div>
</body>
</html>
