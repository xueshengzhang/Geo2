package GeoLite2;

import lombok.Data;

/**
 * @Author xszhang
 * @Date 2023/2/16
 */
@Data
public class IpEntity {
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 洲名
     */
    private String continent;
    /**
     *  ASN自治系统号
     */
    private String autonomousSystemOrganization;
    /**
     * 国家名
     */
    private String countryName;
    /**
     * ISO国家代码
     */
    private String countryCode;
    /**
     * 省/州
     */
    private String provinceName;
    /**
     * ISO省/州 代码
     */
    private String provinceCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
}
