
package com.era.AllCustNo;

/**
 *
 * @author roman
 */

import com.era.AllAgentNo.UserParamBean;
import java.sql.*;
import java.util.*;
import com.era.FingerCheck.*;

public class AllCustNo {
    
   // public static List<Integer> custNo(UserParamBean modal, String pStandard ){
    public static HashMap<Integer, Integer> custNo(UserParamBean modal, String pStandard ){
        
        Connection con = null;
        //List<Integer> listStrCust = new ArrayList<Integer>();
        HashMap<Integer, Integer> custNoHM=new HashMap<Integer, Integer>();
            //System.out.println("modal.getCustNo() :"+ (modal.getCustNo())+" (Expected is 0)");
            //System.out.println("modal.getAgentPoint() :"+ (modal.getAgentPoint()));
        int custNo = 0;
        custNo = modal.getCustNo();
            
        int pointNo = 0;
        pointNo = modal.getAgentPoint();
        try{
            con = DbConnectivity.getConnection();
            CallableStatement cs = con.prepareCall("begin BIOTPL.GET_CUSTOMER_LIST2.GET_CUST_CUST_NO_DYN(?,?,?,?); end;");
            cs.setString(1, pStandard);
            cs.setInt(2, custNo);
            cs.setInt(3, pointNo);
            cs.registerOutParameter(4, java.sql.Types.ARRAY, "CUS_NO_LIST");
            cs.execute();
         
            Array arrCusNo = cs.getArray(4);
            
            Map map = con.getTypeMap();
            map.put("CUSTOMER_NO_LIST", Class.forName("com.era.FingerCheck.TestArr"));
           
            Object[] values = (Object[]) arrCusNo.getArray(); /// Customer No. here.
           
           for (int i = 0; i < values.length; i++) {
                TestArr a = (TestArr) values[i];
                //listStrCust.add(a.attrOne); 
                custNoHM.put(a.attrOne, a.attrTwo);
            }    
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Error is:"+ ex);
            System.out.println("Exception raised at Retrieve Customer No. Error is : "+ ex);
        }
        finally
        {
            DbConnectivity.releaseConnection(con);
        }
         //System.out.println("---Total ("+pStandard+") Customer's : "+listStrCust.size());
     //return listStrCust;       
     return custNoHM;
     
    }
    
// main function for testing this class.    
     public static void main(String[] args) {
         try {
             HashMap<Integer, Integer> custNoHM = AllCustNo.custNo(null,"S");
            /* for (Map.Entry<Integer, Integer> entryCustNo : custNoHM.entrySet()){
                 System.out.println("Key : "+ entryCustNo.getKey());
                 System.out.println("Value : "+ entryCustNo.getValue());
             }*/
             
             System.out.println("Total customer : "+ custNoHM.size());
        /*     
             System.out.println(custNoHM);        //Old 231, 120, 131, 1559, 42083, 44232, 114114
             Map.Entry<Integer,Integer> entry = custNoHM.entrySet().iterator().next();
             int key = entry.getKey();
             int value = entry.getValue();
             System.out.println("Key is : "+ key);
             System.out.println("Value is : "+ value);*/
             
         } catch (Exception e) {
             System.out.println("Error is : "+ e.toString());
             e.printStackTrace();
         }
        
    }
    
}
//258534