/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvscript;

import java.io.File;

/**
 *
 * @author rahul
 */
public class Test {
    public static void main(String[] args) {
        
        ///eForms/Excel/19092022150755_rahulm.96.csv
             String file =    "/eForms/Excel/19092022150755_rahulm.96.csv" ;
             String[] file2 = file.split("/");
             File file1 = new File(file2[3]); 
             String ext = file1.toString();
             String finalFileName = ext.split("(\\.csv)")[0];
             
             //String ext1 = ext.substring(ext.);
             //System.out.println(" file name     "+ext1);
              //String a = "abc.csv";
              System.out.println(ext.split("(\\.csv)")[0]);
    }
}
