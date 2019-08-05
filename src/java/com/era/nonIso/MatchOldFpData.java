package com.era.nonIso;

import com.era.AllAgentNo.ReturnUsersNo;
import com.era.AllAgentNo.UserParamBean;
import java.util.List;
import com.era.AllCustNo.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import process.matchData.MatchFingerInsert;

/**
 *
 * @author roman
 */
public class MatchOldFpData {

    public void matchingOldFpData(UserParamBean modal, String pStandard) {
        AllCustNo allCustNo = new AllCustNo();
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        HashMap<Integer, Integer>  custListHm = allCustNo.custNo(modal, pStandard);  // Receive all Customer No as String
        
        // Get first customer no of a agent point.
        Map.Entry<Integer,Integer> entry = custListHm.entrySet().iterator().next();
        int custNo = entry.getKey();
        
        System.out.println("Total Old Customer :" + custListHm.size());
        //System.out.println("One customer No of above Agent Point : " + custNo);

        // Receive Agent Or ARO no
        ReturnUsersNo returnUsersNo = new ReturnUsersNo();
        HashMap<Integer, Integer> userListHM=new HashMap<Integer, Integer>();
        
        //System.out.println("Agent Point : "+modal.getAgentPoint());
        
        if (modal.getUserType().equals("agent") || modal.getUserType() == "agent") {
            userListHM = returnUsersNo.agentUsers(pStandard, custNo); // Receive agent no list 
            System.out.println("Total Agent : " + userListHM.size());
        } else if (modal.getUserType().equals("aro") || modal.getUserType() == "aro") {
            userListHM = returnUsersNo.aROUsers(pStandard, custNo);  // Receive ARO list
            System.out.println("Total ARO : " + userListHM.size());
        } else if (modal.getUserType().equals("all") || modal.getUserType() == "all") {
            userListHM = returnUsersNo.agentAroUsers(pStandard, custNo); // Receive Agent + ARO list
            System.out.println("Total Agent + ARO : " + userListHM.size());
        }
        
        //System.out.println("Agent User of Point :" + userListHM); // Print all user no in above Agent Point

        // Here write code for matching above Customer and Agent Finger Data.
        if (!userListHM.isEmpty()) {
            //listIntCust 
            // Now write code here for customer loop and agent loop
            FingerMatching fingerMatching = new FingerMatching();
            String status = "O";
            int custYcount = 0; // Finger1 and Finger2 column value(Y/N) count. If both column Y then 2 else 1
            int userYcount = 0;
            FileRead fileRead = new FileRead();
            FingerData custFingerData = null;
            FingerData userFingerData = null;
            String FPavailbe = "YES";

            try {
                //for (int cNo = 0; cNo < custListHm.size(); cNo++) {     // Loop change for new Customer (cNo = Customer No)
                for (Map.Entry<Integer, Integer> entryCustNo : custListHm.entrySet()) {     // Loop change for new Customer (cNo = Customer No)
                    //System.out.println("Retriving Y status for customer(" + custListHm.get(cNo) + ") from database.");
                    FingerCheckInDB fingerCheckInDB = new FingerCheckInDB(); //** need to close this Object.
                    
                /*    Calendar cal1 = Calendar.getInstance();
                    String stringDate1 = sdf.format(cal1.getTime());
                    System.out.println("Before Customer Y Count :"+stringDate1);*/
                    
               //     custYcount = fingerCheckInDB.twoFingerAvailableStatus(custNo); // Check finger Number availibility in directory But match in Database
                    
                /*  Calendar cal2 = Calendar.getInstance();
                    String stringDate2 = sdf.format(cal2.getTime());
                    System.out.println("After Customer Y Count :"+stringDate2);*/
                    
                    
                    //System.out.println("Customer Number : "+ (cNo+1));
                    System.out.println("Customer(" + entryCustNo.getKey() + ") total finger(Y) : " + entryCustNo.getValue());
                    

                    if (entryCustNo.getValue() > 0) {
                        for (int cfn = 1; cfn <= entryCustNo.getValue(); cfn++){ // TThis loop for change finger number of specific Customer
                        
                            //C1_Template = null;
                            custFingerData = null;
                            
                        /*   Calendar cal3 = Calendar.getInstance();
                             String stringDate3 = sdf.format(cal3.getTime());
                             System.out.println("Before Customer image read : "+stringDate3);*/
                            try {
                                custFingerData = fileRead.getFingerDataFromFile(entryCustNo.getKey(), cfn); //Read of Customer Finger Data //258534=[257351]
                                System.out.println("Customer Template Length : "+ custFingerData.getM_Template().length); // This line raise exception if finger data not found. So don't delete this line
                                FPavailbe = "YES";
                            } catch (Exception e) {
                                FPavailbe = "NO";
                                System.out.println("Finger data is not availble in J2EE directory for customer "+entryCustNo.getKey());
                            }
                            
                        /*    Calendar cal4 = Calendar.getInstance();
                              String stringDate4 = sdf.format(cal4.getTime());
                              System.out.println("After Customer image read : "+stringDate4);*/
                            
                            
                            System.out.println("Before if condition.");
                            if(!FPavailbe.equals("NO") || FPavailbe != "NO"){   // Check customer finger read successfully or Not
                                System.out.println("Inside if condition.");
                            for (Map.Entry<Integer, Integer> entryUserNo : userListHM.entrySet()) {         // This loop for change next agent (uNo = User No)

                                //System.out.println("User Number : "+ (uNo+1));
                     //           userYcount = fingerCheckInDB.twoFingerAvailableStatus(userListHM.get(uNo));
                                System.out.println("User(" + entryUserNo.getKey() + ") total finger(Y) : " + entryUserNo.getValue());
                                if (entryUserNo.getValue() > 0) {
                                    for (int ufn = 1; ufn <= entryUserNo.getValue(); ufn++) {  // ufn = agent finger // This loop for change finger number of specific agent

                                        userFingerData = null;
                                        
                                        
                                    /*     Calendar cal5 = Calendar.getInstance();
                                         String stringDate5 = sdf.format(cal5.getTime());
                                         System.out.println("Before user image read : "+stringDate5);*/
                                        try {
                                            userFingerData = fileRead.getFingerDataFromFile(entryUserNo.getKey(), ufn); // Read Agent Finger Data
                                            System.out.println("User Template Length : "+ userFingerData.getM_Template().length);
                                            FPavailbe = "YES";
                                        } catch (Exception e) {
                                            FPavailbe = "NO";
                                            System.out.println("Finger data is not availble in J2EE directory for User "+entryUserNo.getKey());
                                        }
                                        
                                    /*      Calendar cal6 = Calendar.getInstance();
                                          String stringDate6 = sdf.format(cal6.getTime());
                                          System.out.println("After user image read : "+stringDate6); */
                                        
                                        
                                        //if(userFingerData.getM_Template() != null || !userFingerData.getM_Template().equals("")){ // Check Aent finger read successfully or Not
                                        if(!FPavailbe.equals("NO") || FPavailbe != "NO"){ // Check Agent finger read successfully or Not
                                            boolean result = false;
                                            
                                        /*     Calendar cal7 = Calendar.getInstance();
                                             String stringDate7 = sdf.format(cal7.getTime());
                                             System.out.println("Before Match : "+stringDate7);*/
                                            result = fingerMatching.matchFingerData(custFingerData, userFingerData);
                                        /*     Calendar cal8 = Calendar.getInstance();
                                             String stringDate8 = sdf.format(cal8.getTime());
                                             System.out.println("After match : "+stringDate8);*/
                                             
                                            if (result) {
                                            String vCustFpNo = Integer.toString(cfn);
                                            String vAgentFpNo = Integer.toString(ufn);
                                            byte[] fingerDataForInsert = custFingerData.getM_Template(); // here store for insert this match finger data to database
                                            //System.out.println("Old finger data matched Successfully");
                                            //System.out.println("cust No :" +custNo+ "/ Agent NO :"+ agentNo+"/ CustFinger No :"+cfn+"/ AgentFinger No :"+ufn+"/ Finger Data :"+custFingerData);
                                            System.out.println("** Old Finger Matched. Customer(" + entryCustNo.getKey() + " - " + cfn + ") Vs Agent(" + entryUserNo.getKey() + " - " + ufn + ") **");
                                            //matchDataInsertToDB.insertMatchValue(custNo, agentNo, vCustFpNo, vAgentFpNo, fingerDataForInsert);
                                            //MatchFingerInsert.updateStatus();
                                            MatchFingerInsert.insertFinger(entryCustNo.getKey(), entryUserNo.getKey(), vAgentFpNo, vCustFpNo, status);
                                        } else {
                                            System.out.println("## Old Finger does not Matched for Customer(" + entryCustNo.getKey() + " - " + cfn + ") Vs Agent(" + entryUserNo.getKey() + " - " + ufn + ") ##");
                                        }
                                        }else{
                                            System.out.println("*** User("+entryUserNo.getKey()+") Finger is not available in Directory.");
                                        }

                                        //------- Matching Process Start
                                        //System.out.println(custFingerData+"||"+userFingerData);
                                        

                                        //-------
                                    }
                                } else {
                                    System.out.println("Finger not available for User(" + entryUserNo.getKey() + ").");
                                }

                            }
                            }else{
                                 System.out.println("FingerPrint not available for Customer(" + entryCustNo.getKey() + ").");
                            }
                            
                        }

                    } else {
                        System.out.println("Finger not available for Customer(" + entryCustNo.getKey() + ").");
                    }

                }

            } catch (Exception e) {
                System.out.println("Error is = " + e);
            }

        } else {
            System.out.println("Old agent user not found " + modal.getAgentPoint() + " point.");
        }

    }
}
