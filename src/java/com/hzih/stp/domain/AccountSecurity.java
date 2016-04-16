package com.hzih.stp.domain;

import java.util.Date;
import java.util.Set;

/**
 * 帐号，用户
 * 
 * @author collin.code@gmail.com
 * @hibernate.class table="account"
 */
public class AccountSecurity {
	/**
	 * @hibernate.id column="id" generator-class="increment" type="long"
	 *               length="11"
	 */
	Long id;

	boolean isVirusScan;

	boolean isFilter;

    String infoLevel;

    String appName;


	public AccountSecurity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public boolean isVirusScan() {
        return isVirusScan;
    }

    public void setVirusScan(boolean isVirusScan) {
        this.isVirusScan = isVirusScan;
    }

    public boolean isFilter() {
        return isFilter;
    }

    public void setFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    public String getInfoLevel() {
        return infoLevel;
    }

    public void setInfoLevel(String infoLevel) {
        this.infoLevel = infoLevel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
