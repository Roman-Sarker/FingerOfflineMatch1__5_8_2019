package process.matchData;

import java.sql.*;
import com.era.FingerCheck.*;
import java.text.SimpleDateFormat;
import javax.sql.rowset.serial.SerialBlob;

public class MatchFingerInsert {

    public static void storeBackup(){
        Connection con = DbConnectivity.getConnection();
        String sqlCheckTable = "SELECT 1 FROM all_tables\n" +
                "WHERE OWNER = 'BIOTPL'\n" +
                "AND TABLE_NAME = 'FINGER_MATCH_REPORT'"; //"SELECT 1 FROM BIOTPL.FINGER_MATCH_REPORT";
        String sqlCheckBKPtable = "SELECT 1 FROM all_tables\n" +
                "WHERE OWNER = 'BIOTPL'\n" +
                "AND TABLE_NAME = 'FINGER_MATCH_REPORT_BKP'";
        try {
            PreparedStatement ps = con.prepareStatement(sqlCheckTable);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){ // Check FINGER_MATCH_REPORT table is availble or not
                try {
                    ps = con.prepareStatement(sqlCheckBKPtable);
                    rs = ps.executeQuery();
                    if(rs.next()){ // Check FINGER_MATCH_REPORT_BKP table is availble or not
                        try {
                            String sqlBackUp = "INSERT INTO FINGER_MATCH_REPORT_BKP\n SELECT * FROM FINGER_MATCH_REPORT WHERE STATUS = 'OF'"; // OF indicate FingerOfflineMatch in database status column
                            String sqlDeleteData = "DELETE FROM FINGER_MATCH_REPORT WHERE STATUS = 'OF'";
                            
                            Statement st=con.createStatement();
                            st.addBatch(sqlBackUp);
                            st.addBatch(sqlDeleteData);
                            st.executeBatch();
                        } catch (Exception e) {
                            System.out.println("Exception raised at Insert data FINGER_MATCH_REPORT_BKP table from FINGER_MATCH_REPORT table Or raised at Delete data from FINGER_MATCH_REPORT table after insert into FINGER_MATCH_REPORT_BKP table.");
                            System.out.println("Error is : "+ e);
                        }
                        
                        
                    }else{System.out.println("FINGER_MATCH_REPORT_BKP not availble.");}
                } catch (Exception e) {
                    System.out.println("FINGER_MATCH_REPORT_BKP not availble. Error is : "+ e);
                }
                
            }else{System.out.println("FINGER_MATCH_REPORT not availble.");}
        } catch (Exception e) {
            System.out.println("FINGER_MATCH_REPORT not availble. Error is : "+ e);
        }
        
        /*
        PreparedStatement ps = con.prepareStatement(sqlCheckTable);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
        
        String sqlCheckBKPtable = "SELECT 1 FROM all_tables\n" +
        "WHERE OWNER = 'BIOTPL'\n" +
        "AND TABLE_NAME = 'FINGER_MATCH_REPORT_BKP'";
        ps = con.prepareStatement(sqlCheckBKPtable);
        rs = ps.executeQuery();
        System.out.println("FINGER_MATCH_REPORT_BKP found.");
        if(rs.next())
        {
        String sqlDropBKPTable = "DROP TABLE BIOTPL.FINGER_MATCH_REPORT_BKP CASCADE CONSTRAINT";
        ps = con.prepareStatement(sqlDropBKPTable);
        ps.execute();
        System.out.println("FINGER_MATCH_REPORT_BKP table drop successfully.");
        }
        else{
        System.out.println("FINGER_MATCH_REPORT_BKP is not availabale.");
        }
        String sqlBackup = "CREATE TABLE BIOTPL.FINGER_MATCH_REPORT_BKP AS\n" +
        "   (SELECT * FROM BIOTPL.FINGER_MATCH_REPORT)";
        
        String sqlTrcMainTable = "TRUNCATE TABLE BIOTPL.FINGER_MATCH_REPORT";
        
        Statement st=con.createStatement();
        st.addBatch(sqlBackup);
        st.addBatch(sqlTrcMainTable);
        st.executeBatch();
        
        }
        else{
        System.out.println("FINGER_MATCH_REPORT table is empty.");
        }*/
        DbConnectivity.releaseConnection(con);
    
    }
    public static void insertFinger(int pCustId, int pAgentId, String pAgentFP, String pCustFP, String pStatus) throws SQLException {
       
        //Date d = new SimpleDateFormat("dd/MMM/yyyy").parse(date);
        int vCustId = pCustId;
        int vAgentId = pAgentId;
        String vAgentFP = pAgentFP;
        String vCustFP = pCustFP;
        
        String vStandard = pStatus;
        String vStatus = "OF";
        Connection con = DbConnectivity.getConnection();
        try {
            
            String sql = "INSERT INTO BIOTPL.finger_match_report(AGENT_CUST_NO, CUSTOMER_CUST_NO, CREATE_DATE, CREATE_BY, AGENT_FINGER, CUSTOMER_FINGER, CUSTOMER_FINGER_DATA,  STANDARD, STATUS)\n"
                    + "                                   VALUES("+vAgentId+",   "+vCustId+",        sysdate,       NULL,    '"+vAgentFP+"',    '"+vCustFP+"',         null,   '"+vStandard+"', '"+vStatus+"')"; //"+vFPdata+"
           
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("SQLException error is : "+e.getMessage());
            System.out.println("Exception raise at insert matched finger data. Error is : "+e);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception Error is: " + ex.getMessage());
            System.out.println("Exception raise at insert matched finger data. Error is : "+ex);
        }
        finally{
            DbConnectivity.releaseConnection(con);
        }
    }
}
//AGENT_CUST_NO, AGENT_FINGER, CREATE_BY, CREATE_DATE, CUSTOMER_CUST_NO, CUSTOMER_FINGER, CUSTOMER_FINGER_DATA, DEVICE_ID, STANDARD
