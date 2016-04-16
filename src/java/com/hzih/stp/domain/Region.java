package com.hzih.stp.domain;

/**

 */
public class Region {
	
	/**
	 */
    Long regionId;
	
	/**
	 * 地区名
	 */
	String regionName;

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
