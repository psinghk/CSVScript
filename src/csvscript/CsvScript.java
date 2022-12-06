/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Gyan Pc
 */
public class CsvScript {

//    private static final String dbhost = "localhost";
//    private static final String dbuser = "root";
//    private static final String dbname = "onlineform_live";
////    private static final String dbpass = "root@123";
//    private static final String dbpass = "";

    private static final String dbhost = "192.168.1.102";
    private static final String dbuser = "root";
    private static final String dbname = "onlineform";
    private static final String dbpass = "@Forms@123";
    private static Connection con = null;

    private static final String url = "jdbc:mysql://" + dbhost + "/" + dbname
            + "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
    private static final String url1 = "jdbc:mysql://" + dbhost
            + "/information_schema?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";

    static String finalFileName = "";
    static Boolean fileExist = false;

    public static void main(String[] args) throws SQLException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(url, dbuser, dbpass);
            System.out.println("Database Connection Created");

            ArrayList<csvbean> regNos = fetchregNo();
            for (csvbean regNo : regNos) {
                System.out.println(" All regNo between given Date  is     "+regNo.getRegistration_no());
            }
             fetchvalidCsv(con, regNos);
             fetchNotValidCsv(con, regNos);
             fetchErrorCsv(con, regNos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

    private static String fetchvalidCsv(Connection con, ArrayList<csvbean> listRegno) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;

        try {
            //ArrayList<csvbean> listRegno = fetchregNo();
            String qry = "";
            for (csvbean bean : listRegno) {
                String fil = bean.getRenamed_filepath();
                String[] file2 = fil.split("/");
                File file1 = new File(file2[3]);
                String ext = file1.toString();
                finalFileName = ext.split("(\\.csv)")[0];
                File f = new File("//eForms//Excel//" + finalFileName + "-valid.csv");
                if (!f.exists()) {
                    qry = "select * from bulk_users where registration_no=? AND error_status='0'";
                    pst = con.prepareStatement(qry);
                    pst.setString(1, bean.getRegistration_no());
//                    String datetime = bean.getDatetime();
//                    String datetime1[] = datetime.split(" ");
//                    String date = datetime1[0];
//                    String time = datetime1[1];
//                    String date1[] = date.split("-");
//                    int year = Integer.parseInt(date1[0]);
//                    int month = Integer.parseInt(date1[1]);
//                    int day = Integer.parseInt(date1[2]);
//                    String time1[] = time.split(":");
//                    int hour = Integer.parseInt(time1[0]);
//                    int min = Integer.parseInt(time1[1]);
//                    int sec = Integer.parseInt(time1[2].substring(0, 2));
//                    LocalDateTime now = LocalDateTime.of(year, month, day, hour, min, sec);
//                    DateTimeFormatter dt = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
//                    String pdate = dt.format(now);
                    if (pst != null) {
                        rs1 = pst.executeQuery();
                        String FilePath = "//eForms//Excel//" + finalFileName + "-valid.csv";
                        FileWriter file3 = new FileWriter(FilePath);
                        CSVWriter writer = new CSVWriter(file3);
                        System.out.println("create valid file" + writer.toString());
                        ResultSetMetaData Mdata = rs1.getMetaData();
                        Mdata.getColumnName(1);
                        String line1[] = {Mdata.getColumnName(4), Mdata.getColumnName(5), Mdata.getColumnName(6), Mdata.getColumnName(7), Mdata.getColumnName(8), Mdata.getColumnName(9), Mdata.getColumnName(10), Mdata.getColumnName(11), Mdata.getColumnName(12), Mdata.getColumnName(13), Mdata.getColumnName(15)};
                        writer.writeNext(line1);
                        String data[] = new String[11];
                        while (rs1.next()) {
                            data[0] = rs1.getString("fname");
                            data[1] = rs1.getString("lname");
                            data[2] = rs1.getString("designation");
                            data[3] = rs1.getString("department");
                            data[4] = rs1.getString("state");
                            data[5] = rs1.getString("mobile");
                            data[6] = rs1.getString("dor");
                            data[7] = rs1.getString("uid");
                            data[8] = rs1.getString("mail");
                            data[9] = rs1.getString("dob");
                            data[10] = rs1.getString("emp_code");
                            writer.writeNext(data);

                        }
                        writer.flush();
                    }
                } else {
                    System.out.println("valid file exist");
                }
            }

            System.out.println("csv::::::::::::" + pst);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    private static String fetchNotValidCsv(Connection con, ArrayList<csvbean> listRegno) {
        PreparedStatement pst = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        try {
            //ArrayList<csvbean> listRegno = fetchregNo();
            String qry = "";
            for (csvbean bean : listRegno) {
                String fil = bean.getRenamed_filepath();
                String[] file2 = fil.split("/");
                File file1 = new File(file2[3]);
                String ext = file1.toString();
                finalFileName = ext.split("(\\.csv)")[0];
                File f = new File("//eForms//Excel//" + finalFileName + "-notvalid.csv");
                if (!f.exists()) {
                    qry = "select * from bulk_users where registration_no=? AND error_status='1'";
                    pst = con.prepareStatement(qry);
                    pst.setString(1, bean.getRegistration_no());
//                    String datetime = bean.getDatetime();
//                    String datetime1[] = datetime.split(" ");
//                    String date = datetime1[0];
//                    String time = datetime1[1];
//                    String date1[] = date.split("-");
//                    int year = Integer.parseInt(date1[0]);
//                    int month = Integer.parseInt(date1[1]);
//                    int day = Integer.parseInt(date1[2]);
//                    String time1[] = time.split(":");
//                    int hour = Integer.parseInt(time1[0]);
//                    int min = Integer.parseInt(time1[1]);
//                    int sec = Integer.parseInt(time1[2].substring(0, 2));
//                    LocalDateTime now = LocalDateTime.of(year, month, day, hour, min, sec);
//                    DateTimeFormatter dt = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
//                    String pdate = dt.format(now);
                    if (pst != null) {
                        rs1 = pst.executeQuery();
                        if (rs1 != null);
                        {
                            CSVWriter writer = new CSVWriter(new FileWriter("//eForms/Excel//" + finalFileName + "-notvalid.csv"));
                            System.out.println("create notvalid file" + writer);
                            ResultSetMetaData Mdata = rs1.getMetaData();
                            Mdata.getColumnName(1);
                            String line1[] = {Mdata.getColumnName(4), Mdata.getColumnName(5), Mdata.getColumnName(6), Mdata.getColumnName(7), Mdata.getColumnName(8), Mdata.getColumnName(9), Mdata.getColumnName(10), Mdata.getColumnName(11), Mdata.getColumnName(12), Mdata.getColumnName(13), Mdata.getColumnName(28)};
                            writer.writeNext(line1);
                            String data[] = new String[11];
                            while (rs1.next()) {
                                data[0] = rs1.getString("fname");
                                data[1] = rs1.getString("lname");
                                data[2] = rs1.getString("designation");
                                data[3] = rs1.getString("department");
                                data[4] = rs1.getString("state");
                                data[5] = rs1.getString("mobile");
                                data[6] = rs1.getString("dor");
                                data[7] = rs1.getString("uid");
                                data[8] = rs1.getString("mail");
                                data[9] = rs1.getString("dob");
                                data[10] = rs1.getString("email_error");
                                writer.writeNext(data);
                            }
                            writer.flush();
                        }
                    }
                }else {
                    System.out.println("Not valid file exist");
                }
            }

            System.out.println("csv::::::::::::" + pst);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    private static String fetchErrorCsv(Connection con, ArrayList<csvbean> listRegno) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
//            ArrayList<csvbean> listRegno = fetchregNo();
            String qry = "";

            for (csvbean bean : listRegno) {
                String fil = bean.getRenamed_filepath();
                String[] file2 = fil.split("/");
                File file1 = new File(file2[3]);
                String ext = file1.toString();
                finalFileName = ext.split("(\\.csv)")[0];
                File f = new File("//eForms//Excel//" + finalFileName + "-error.csv");
                if (!f.exists()) {
                    qry = "select email_error from bulk_users where registration_no=?";
                    pst = con.prepareStatement(qry);
                    pst.setString(1, bean.getRegistration_no());
                    if (pst != null) {
                        rs1 = pst.executeQuery();
//                        String datetime = bean.getDatetime();
//                        String datetime1[] = datetime.split(" ");
//                        String date = datetime1[0];
//                        String time = datetime1[1];
//                        String date1[] = date.split("-");
//                        int year = Integer.parseInt(date1[0]);
//                        int month = Integer.parseInt(date1[1]);
//                        int day = Integer.parseInt(date1[2]);
//                        String time1[] = time.split(":");
//                        int hour = Integer.parseInt(time1[0]);
//                        int min = Integer.parseInt(time1[1]);
//                        int sec = Integer.parseInt(time1[2].substring(0, 2));
//                        LocalDateTime now = LocalDateTime.of(year, month, day, hour, min, sec);
//                        DateTimeFormatter dt = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
//                        String pdate = dt.format(now);
                        CSVWriter writer = new CSVWriter(new FileWriter("//eForms/Excel//" + finalFileName + "-error.csv"));
                        System.out.println("create error file" + writer);
                        ResultSetMetaData Mdata = rs1.getMetaData();
                        Mdata.getColumnName(1);
                        String line1[] = {Mdata.getColumnName(1)};
                        writer.writeNext(line1);
                        String data[] = new String[1];
                        while (rs1.next()) {
                            data[0] = rs1.getString("email_error");
                            writer.writeNext(data);
                        }
                        writer.flush();
                    }
                }else {
                    System.out.println("Error file exist");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    public static ArrayList<csvbean> fetchregNo() {
        ArrayList<csvbean> list = new ArrayList<csvbean>();
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        try {
            String qry = "";
            qry = "select auth_email,registration_no,renamed_filepath,datetime from bulk_registration where datetime BETWEEN '2022-09-27' AND '2022-10-27'";
            //qry = "select auth_email,registration_no,renamed_filepath,datetime from bulk_registration where datetime ='2022-09-28 13:09:25'";
            ps = con.prepareStatement(qry);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                csvbean file = new csvbean();
                file.setAuth_email(rs1.getString("auth_email"));
                file.setRegistration_no(rs1.getString("registration_no"));
                file.setRenamed_filepath(rs1.getString("renamed_filepath"));
                file.setDatetime(rs1.getString("datetime"));
                list.add(file);
            }
           

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;

    }
}
