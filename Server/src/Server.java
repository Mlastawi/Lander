//package com.company;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
        import java.io.*;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.lang.String;
		//import com.company.commands;


/**
 * Class implementing server
 */
public class Server implements Runnable {

    private ServerSocket serverSocket;

    Properties scores = new Properties();
    Properties level_list = new Properties();
    PrintWriter outp;
    String[] splited;

    /**
     * default constructor
     * loads ports, highscores and other data from files
     */
    public Server() {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("propsy.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int port = Integer.parseInt(prop.getProperty("port"));
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("waitinh");
        } catch (IOException e) {
            System.err.println("Error starting Server.");
            System.exit(1);
        }

        try {
            scores.load(new FileInputStream("scores.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            level_list.load(new FileInputStream("level_list.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }
    /**
     * run method of the server thread
     * listens to the data from the port and calls for check method
     */
    public void run() {
        while (true)
            try {
                System.out.print(" odbieram");
                Socket clientSocket = serverSocket.accept();


                BufferedReader inp;
                inp=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String str;
                str=inp.readLine();
                System.out.print(str);
                splited = str.split("\\s+");
                outp=new PrintWriter(clientSocket.getOutputStream());

                checkinp(splited[0]);

            } catch (IOException e) {
                System.err.println("error with connection");
            }


        }
    /**
     * check method
     * compare first word(without parameters) from the input data to the lines from commands and send the answers
     */
    public void checkinp(String inp){

        if(inp.equals(commands.highscores)){
            for(int i=1;i<6;i++) {
                System.out.println("zasd");
                outp.println(scores.getProperty(Integer.toString(i)));
                outp.flush();
                //scores.list(System.out);
            }
        }
        else if(inp.equals(commands.best_request)){
            for(int i=1; i<6; i++) {
                String tmp = scores.getProperty(Integer.toString(i));
                String[] temp = tmp.split("\\s+");
                if( Integer.parseInt(temp[1]) < Integer.parseInt(splited[2])) {
                    for(int j=6; j>i+1;j--) {
                        scores.setProperty(Integer.toString(j-1),scores.getProperty(Integer.toString(j-2)) );
                    }
                    scores.setProperty(Integer.toString(i), splited[1] + ' ' + splited[2]);
                    System.out.println("zmieniam");
                    break;

                }

                System.out.println(splited[1]);

            }

        }
        else if(inp.equals(commands.level_list_request)){
            int tmp = Integer.parseInt(level_list.getProperty("amount"));
            outp.println(tmp);
            outp.flush();
        }
        else if(inp.equals(commands.level_request)){

            Properties level_tmp = new Properties();
            try {
                level_tmp.load(new FileInputStream(splited[1]+".properties"));
                System.out.println("wczuttalem ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            level_tmp.list(outp);
            outp.flush();
        }
        else{


        }
    }


    /**
     * main function
     * gets the port and starts a thread
     */
    public static void main(String args[]) {
        Properties p = new Properties();
        String pName = "Server.properties";
        try {
            p.load(new FileInputStream(pName));
        } catch (Exception e) {
            p.put("port", "10023");
        }
        try {
            p.store(new FileOutputStream(pName), null);
        } catch (Exception e) {
        }
        new Server();
    }


}