package GeoLite2;

import com.alibaba.fastjson.JSON;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import org.apache.log4j.Logger;

/**
 * @Author xszhang
 * @Date 2023/2/16
 */

public class ResolveIp {
    private static final String GEO_LITE_CITY_DB_FILE = "C:\\Users\\legion\\Desktop\\fsdownload\\GeoLite2-City.mmdb";
    private static final String GEO_LITE_ASN_DB_FILE = "C:\\Users\\legion\\Desktop\\fsdownload\\GeoLite2-ASN.mmdb";
    private volatile static ResolveIp resolveIp;
    private DatabaseReader cityReader;
    private DatabaseReader asnReader;
    private static Logger log = Logger.getLogger(ResolveIp.class);

    /**
     *  文件装载
     */
    private ResolveIp(){
        try{
            File cityFile = new File(GEO_LITE_CITY_DB_FILE);
            cityReader = new DatabaseReader.Builder(cityFile).build();
            File asnFile = new File(GEO_LITE_ASN_DB_FILE);
            asnReader = new DatabaseReader.Builder(asnFile).build();
        }catch (IOException e){
            log.error("装载失败:"+ e.getMessage());
        }
    }
    /**
     * 双重检索单例
     */
    public static ResolveIp getResolveIp(){
        if(resolveIp==null){
            synchronized (ResolveIp.class){
                if(resolveIp==null){
                    resolveIp = new ResolveIp();
                }
            }
        }
        return resolveIp;
    }

    /**
     * 获取geo2数据信息
     * @param ipAddress
     * @return
     */
    public IpEntity getResolveInfo(String ipAddress)  {
        try{
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse cityResponse = cityReader.city(inetAddress);
            AsnResponse asnResponse = asnReader.asn(inetAddress);
            Country country = cityResponse.getCountry();
            Postal postal =  cityResponse.getPostal();
            City city =  cityResponse.getCity();
            Location location =  cityResponse.getLocation();
            Traits traits = cityResponse.getTraits();
            Subdivision subdivision = cityResponse.getMostSpecificSubdivision();
            Continent continent = cityResponse.getContinent();
            // 封装IP信息实体类
            IpEntity ipEntity = new IpEntity();
            ipEntity.setContinent(continent.getNames().get("zh-CN"));
            ipEntity.setCityName(city.getNames().get("zh-CN"));
            ipEntity.setCountryName(country.getNames().get("zh-CN"));
            ipEntity.setCountryCode(country.getIsoCode());
            ipEntity.setPostalCode(postal.getCode());
            ipEntity.setProvinceName(subdivision.getNames().get("zh-CN"));
            ipEntity.setProvinceCode(subdivision.getIsoCode());
            ipEntity.setLatitude(location.getLatitude());
            ipEntity.setLongitude(location.getLongitude());
            ipEntity.setIpAddress(traits.getIpAddress());
            ipEntity.setAutonomousSystemOrganization(asnResponse.getAutonomousSystemOrganization());
            log.info(JSON.toJSONString(cityResponse));
            log.info(JSON.toJSONString(ipEntity));
            return ipEntity;
        } catch (UnknownHostException e) {
            log.error("未知网络地址:"+ipAddress);
        }  catch (GeoIp2Exception | IOException e) {
            log.error("geo2解析错误："+e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        ResolveIp.getResolveIp().getResolveInfo("218.22.32.186");
        ResolveIp.getResolveIp().getResolveInfo("::7040:779D");
        ResolveIp.getResolveIp().getResolveInfo("::FFFF:7040:779D");

    }
}
