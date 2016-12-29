package com.qa.framework.library.sms;

import com.qa.framework.config.PropConfig;
import com.qa.framework.library.httpclient.HttpMethod;
import com.qa.framework.library.httpclient.Param;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class sendMessage {
    public static String sendMsg(String mobile,String message) throws  IOException {
        String url="http://sdk2.entinfo.cn/webservice.asmx/SendSMS";
        String levedMsg=message;
        String sendMsg=null;
        String returnMsg=null;
        boolean sendSuccess=true;
        while (levedMsg.length()>70){
            sendMsg=message.substring(0,70);
            levedMsg=message.substring(70);
            List<Param> params=addParams(mobile,sendMsg);
            returnMsg=HttpMethod.usePostMethod(url,params,false,false);
            if (!returnMsg.contains("成功")){
                sendSuccess=false;
                break;
            }
        }
        List<Param> params=addParams(mobile,levedMsg);
        String result= HttpMethod.usePostMethod(url,params,false,false);
        if (result.contains("成功")&&sendSuccess){
            return mobile+"发送短信成功";
        }else{
            return mobile+"发送短信失败，错误代码为:"+result+returnMsg;
        }
    }

    private static List<Param> addParams(String mobile, String message) {
        List<Param> params=new ArrayList<Param>();
        Param param1=new Param();
        param1.setName("sn");
        param1.setValue(PropConfig.getSN());
        Param param2=new Param();
        param2.setName("pwd");
        param2.setValue(PropConfig.getSNPWD());
        Param param3=new Param();
        param3.setName("mobile");
        param3.setValue(mobile);
        Param param4=new Param();
        param4.setName("content");
        param4.setValue(message);
        params.add(param1);
        params.add(param2);
        params.add(param3);
        params.add(param4);
        return params;
    }

    public static String sendMsg(List<String> mobiles,String message) throws IOException {
        String afterSend="短信结果：\n";
        for(String mobile:mobiles){
            String reslut=sendMsg(mobile,message);
            if (reslut.contains("失败")){
                afterSend=afterSend+reslut+"\n";
            }
        }
        return afterSend;
    }
}
