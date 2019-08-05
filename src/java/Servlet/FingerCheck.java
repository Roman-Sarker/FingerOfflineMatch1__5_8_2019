package Servlet;

import com.era.AllAgentNo.StandardMatching;
import com.era.AllAgentNo.UserParamBean;
import com.era.AllCustNo.AllCustNo;
import com.era.FingerCheck.DbConnectivity;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Class.forName;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.era.FingerCheck.DbConnectivity;
import com.era.sQuery.BeanClass;
import process.matchData.GetFingerData;
import process.matchData.MatchFingerInsert;

/**
 *
 * @author roman
 */
@WebServlet(name = "FingerCheck", urlPatterns = {"/FingerCheck"})
public class FingerCheck extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter out = res.getWriter();
        System.out.println("Get-Method");
        
        try {
            //BackUp previous data.
             MatchFingerInsert.storeBackup();
        } catch (Exception e) {
            System.out.println("Error at Back up privious matched data. Error is : "+ e);
        }
        
        // Default initialization of variable
        int agentPoint = 0;
        int custNo = 0;
        String allCust = "0";
        String userType = "0";
        
                // Receive and assign parameter value in variable
        try {
            
            System.out.println("Receive Request Data.");
            String agentPointStr = req.getParameter("agentPoint"); 
            String custNoStr = req.getParameter("custNo"); 
            allCust = req.getParameter("allCust"); 
            userType = req.getParameter("userType"); 
            
            // Check and initialize agent point
            //System.out.println("Checking agentPoint.");
            if(agentPointStr.equals("") || agentPointStr.equals(null) || agentPointStr == ""){
                agentPoint = 0;
                System.out.println("agentPoint :"+ agentPoint);
            }else{
                agentPoint = Integer.parseInt(agentPointStr);
                System.out.println("agentPoint = "+ agentPoint);
            }
            // Check and initialize Cust No
            //System.out.println("Checking custNo.");
            if(custNoStr.equals("") || custNoStr.equals(null) || custNoStr == ""){
                custNo = 0;
                System.out.println("custNo :"+ custNo);
            }else{
                System.out.println("custNoStr :"+custNoStr);
                try {
                    custNo = Integer.parseInt(custNoStr);
                    
                } catch(NumberFormatException nf){
                    System.out.println("Not valid customer number. Error is : "+nf);
                } 
                catch (Exception e) {
                    System.out.println("Cust No parsing problem. error is : "+e);
                }
                
                System.out.println("custNo = " + custNo);
            }
            
            
            // Check and initialize All Cust No
            //System.out.println("Checking all Cust no.");
            if(allCust.equals("") || allCust.equals(null) || allCust == ""){
                allCust = "0";
                System.out.println("allCust :"+ allCust);
            }else{
                allCust = allCust;
                System.out.println("allCust = " + allCust);
            }
            // Check User Type
            //System.out.println("Checking userType");
            if(userType.equals("") || userType.equals(null) || userType == ""){
                System.out.println("User type not found. Please stop the process = " + custNoStr);
                userType = "User type not found";
            }
            
        } catch (Exception e) {
            System.out.println("Exception raised to receive parameter from Finger_Matching.jsp page.");
            System.out.println("Error is :"+ e);
            e.printStackTrace();
        }
         
/*
        int agentPoint = 0; // Defult value 0 zero
        int custNo = 11220; // Defult value 0 zero
        String allCust = "0"; // Defult value 0 zero
        String userType = "agent"; // value - 'agent','aro','all'
*/

        // Initialize parameter value in modal class
        UserParamBean modal = new UserParamBean();
        modal.setAgentPoint(agentPoint);
        modal.setCustNo(custNo);
        modal.setAllCustomer(allCust);
        modal.setUserType(userType);

        Connection con = null;

        try {
                
            
            if (modal.getCustNo() != 0) {        //*** Process for single customer.
                System.out.println("Process start for single customer.");
                String sql = "SELECT STANDARD FROM BIOTPL.FP_ENROLL A, EMOB.MB_CUSTOMER_MST B WHERE A.CUST_NO = " + modal.getCustNo() + " AND A.CUST_NO = B.CUST_NO";
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    con = DbConnectivity.getConnection();
                    pstmt = con.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    if (rs != null) {
                    //if (rs.first()) {
                        System.out.println("Data found.");  
                        while (rs.next()) {
                          System.out.println("Inside while condition.");  
                            String standard = rs.getString("STANDARD");
                            if (standard.equals("S")) {
                                GetFingerData.matchFingerData("S", modal);
                            } else if ((standard.equals("O"))) {
                                GetFingerData.matchFingerData("O", modal);
                            }
                        }
                    }else{
                            System.out.println("Invalid customer no. else");
                            res.sendRedirect("admin/Finger_Matching.jsp?eMessage=" + "Invalid Customer Code.");  
                         }
                } catch (Exception e) {
                    System.out.println("Invalid customer no. Error is : "+e);
                    res.sendRedirect("admin/Finger_Matching.jsp?eMessage=" + "Invalid Customer Code.");  
                    //System.exit(0);
                    
                    /*String eMessage = "Invalid Customer Code.";
                    req.setAttribute("eMessage", eMessage);
                    req.getRequestDispatcher("admin/Finger_Matching.jsp").forward(req, res);*/
                } finally {
                    rs.close();
                    pstmt.clearParameters();
                    pstmt.close();
                }

            } else {   //*** Process for agent point or all customer.
                

                // ***Check for ISO (Standard) data
                System.out.println("Before process for Standard Finger Data.");
                GetFingerData.matchFingerData("S", modal);
                // ***Check for OLD futronic data
                System.out.println("Before process for Old Finger Data.");
                GetFingerData.matchFingerData("O", modal);
            }
        } catch (Exception e) {
            System.out.println("Exception raised at Servler(FingerCheck.java)");
            out.println("Error");
            e.printStackTrace();
        } finally {

        }

///*** Select query for report view     
        try {
            con = DbConnectivity.getConnection();
            //String sql = "SELECT AGENT_CUST_NO, CUSTOMER_CUST_NO, CUSTOMER_FINGER, AGENT_FINGER, STANDARD FROM FINGER_MATCH_REPORT WHERE STATUS = 'Y'";
            //String sql = "SELECT AGENT_CUST_NO, CUSTOMER_CUST_NO, CUSTOMER_FINGER, AGENT_FINGER, STANDARD FROM FINGER_MATCH_REPORT";
            String sql = "SELECT BIOTPL.USRINFOFP (AGENT_CUST_NO) AS \"USER\", BIOTPL.USRINFOFP (CUSTOMER_CUST_NO) AS \"CUSTOMER\",AGENT_FINGER, CUSTOMER_FINGER,  STANDARD FROM FINGER_MATCH_REPORT WHERE STATUS = 'OF'";
            
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            List<BeanClass> myList = new ArrayList<>();

            int count = 0;
            out.println("Customer No, Agent No,  Customer FP No, Agent FP No,  Standard");

            while (rs.next()) {
                BeanClass beanVar = new BeanClass();

                beanVar.setCustNo(rs.getString("CUSTOMER")); //CUSTOMER_CUST_NO
                beanVar.setAgentNo(rs.getString("USER"));    // AGENT_CUST_NO
                beanVar.setCustFpNo(rs.getString("CUSTOMER_FINGER")); // CUSTOMER_FINGER
                beanVar.setAgentFpNo(rs.getString("AGENT_FINGER")); // AGENT_FINGER
                beanVar.setStandard(rs.getString("STANDARD")); //STANDARD

                myList.add(beanVar);
                count = count + 1;
            }
            out.println("\n\nTotal match :" + count);

            req.setAttribute("myList", myList);
            req.getRequestDispatcher("admin/matchDataReport.jsp").forward(req, res);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Exception raised at retrieve matched finger data for show report. Error is : " + ex);
        } finally {
            DbConnectivity.releaseConnection(con);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
