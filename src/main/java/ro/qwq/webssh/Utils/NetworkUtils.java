package ro.qwq.webssh.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ro.qwq.webssh.constant.MagicString;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * <p>
 * NetworkUtils - TODO
 * </p>
 *
 * @author JiuyeXD
 * @version 1.0
 * @since 2022/1/6
 */
@Component
@Log4j2
public class NetworkUtils {

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/6 9:28
     * @Description: 获取远程访问者的IP
     * @param request
     * @return java.lang.String
     */
    public static String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals(MagicString.LOCAL_IPV4_ADDRESS) || ipAddress.equals(MagicString.LOCAL_IPV6_ADDRESS)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length() > MagicString.IP_ADDRESS_LENGTH){
            //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/6 15:04
     * @Description: 根据ip从百度API中获取地址
     * @param strIP ip
     * @return java.lang.String
     */
    public static String getAddressByBD2(String strIP) {
        try {
            URL url = new URL("http://opendata.baidu.com/api.php?query=" + strIP+"&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");;
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            System.out.println(result.toString());
            JSONObject jsStr = JSONObject.parseObject(result.toString());
            JSONArray jsData = (JSONArray) jsStr.get("data");
            JSONObject data= (JSONObject) jsData.get(0);
            return (String) data.get("location");
        } catch (IOException e) {
            return "读取失败";
        }
    }

    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/7 9:24
     * @Description: 去除地址中的网络运营商
     * @param str
     * @return java.lang.String
     */
    public static String removeNetworkOperator(String str){
        StringBuilder sb = new StringBuilder(str);
        if(MagicString.CHINA_NETWORK_OPERATORS.stream().anyMatch(item -> sb.indexOf(item) > -1)){
            String operator = MagicString.CHINA_NETWORK_OPERATORS.stream().filter(item -> sb.indexOf(item) > -1).findFirst().get();
            log.info("operator: {}", operator);
            sb.setLength(str.indexOf(operator) - 1);
        }
        return sb.toString();
    }


    /**
     * @Author: JiuyeXD
     * @Date: 2022/1/7 9:24
     * @Description: 排空
     * @param o target
     * @return java.lang.String
     */
    public static String isNull(Object o){
        if(o == null || "".equals(o.toString().trim())){
            return " ";
        }
        return o.toString();
    }

}
