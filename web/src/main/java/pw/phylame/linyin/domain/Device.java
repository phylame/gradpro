/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.domain;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.pojo.CachedPojo;
import pw.phylame.linyin.pojo.Expireable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Device extends CachedPojo implements Expireable {
    private static final long serialVersionUID = -8436929597599395589L;

    private String clientId;
    private String name;
    private long authorId;
    private String description;
    private int platformType;
    private String platformName;
    private Date createTime;
    private Date expiredTime;

    @Override
    public String getMemkey() {
        return super.getMemkey() + clientId;
    }

    @Override
    public Date getExpiration() {
        return expiredTime;
    }
}
