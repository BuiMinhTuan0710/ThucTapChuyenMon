package com.example.database;

import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class connect {
    private String URL = "http://www.minhtuan.somee.com/myService.asmx/";
    private String functionName;

    public connect(String functionName) {
        this.functionName = functionName;
    }

    public connect() {
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    public HttpURLConnection getConnect()
    {
        try {
            URL url = new URL(URL+functionName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            return connection;
        }
        catch (Exception e)
        {
            Log.e("lỗi",e.getMessage() );
        }
        return null;
    }
    public HttpURLConnection setParameter(String parameter)
    {
        try {
            HttpURLConnection connection = getConnect();
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter ws = new OutputStreamWriter(os,"UTF-8");
            ws.write(parameter);
            ws.flush();
            ws.close();
            return connection;
        }
        catch (Exception e)
        {
            Log.e("lỗi",e.getMessage() );
        }
        return null;
    }

    public NodeList getData(String table)
    {
        try {
            HttpURLConnection connection = getConnect();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connection.getInputStream());
            NodeList nodeList = document.getElementsByTagName(table);
            return nodeList;
        }
        catch (Exception e)
        {
            Log.e("lỗi",e.getMessage() );
        }
        return null;
    }
    public NodeList getDataParameter(String parameter,String table)
    {
        try {
            HttpURLConnection connection = setParameter(parameter);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connection.getInputStream());
            NodeList nodeList = document.getElementsByTagName(table);
            return nodeList;
        }
        catch (Exception e)
        {
            Log.e("lỗi",e.getMessage() );
        }
        return null;
    }

}
