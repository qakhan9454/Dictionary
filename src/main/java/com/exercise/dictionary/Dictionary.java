package com.exercise.dictionary;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

public final class Dictionary {

    static String url = "jdbc:mysql://localhost:3306/file";
    static int count;
    static String Vowel = "AEIOU";
    static String Consonant = "BCDFGHJKLMNPQRSTVWXYZ";
    static StringBuffer word;
    static Connection con;
    static ResultSet rs;
    static PreparedStatement preparedStatement;
    static Random randomGenerator = new Random();

    public static final void main(String[] args){
        String str = "";
        for (int i = 0;i<5;i++){
            char a = getRandomConsonant();
            str = str + a;}
            for(int i = 0;i<3;i++){
                char a = getRandomVowel();
                str = str + a;}
                StringBuffer strBuf = new StringBuffer(str);
            count = 0;
            isEnglishWord(strBuf,str.length());

            if(count>=1){
                try
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    try
                    {
                        con = DriverManager.getConnection(url,"root","1234");
                        preparedStatement = con.prepareStatement("insert into FILE.WORDS values (?,?)");
                        preparedStatement.setString(1, str);
                        preparedStatement.setInt(2, count);
                        preparedStatement.executeUpdate();
                    }

                    catch (SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                catch(ClassNotFoundException e)
                {
                    System.out.println(e);
                }
            }

    }

    private static char getRandomVowel(){
        int randomInt = randomGenerator.nextInt(4);
        return Vowel.charAt(randomInt);
    }
    private static char getRandomConsonant(){
        int randomInt = randomGenerator.nextInt(20);
        return Consonant.charAt(randomInt);
    }
    private  static void swap(StringBuffer str, int pos1, int pos2){
        char t1 = str.charAt(pos1);
        str.setCharAt(pos1, str.charAt(pos2));
        str.setCharAt(pos2, t1);
    }
    private static void isEnglishWord(StringBuffer str, int index){

        if(index == 0)
        {
            System.out.println(str);
            try
            {
                String word = str.toString();
                Class.forName("com.mysql.jdbc.Driver");
                try
                {
                    con = DriverManager.getConnection(url,"root","1234");
                    preparedStatement = con.prepareStatement("SELECT * FROM DICT WHERE word=?");
                    preparedStatement.setString(1, word);
                    rs = preparedStatement.executeQuery();
                    rs = preparedStatement.getResultSet();
                    if(rs.next()){
                        count++;
                    }
                }

                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            catch(ClassNotFoundException e)
            {
                System.out.println(e);
            }
        }


        else {
            isEnglishWord(str, index-1);
            int currPos = str.length()-index;
            for (int i = currPos+1; i < str.length(); i++) {
                swap(str,currPos, i);
                isEnglishWord(str, index-1);
                swap(str,i, currPos);
            }
        }
    }
}

