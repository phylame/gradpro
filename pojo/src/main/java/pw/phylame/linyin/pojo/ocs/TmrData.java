/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo.ocs;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import pw.phylame.linyin.pojo.CachedPojo;
import pw.phylame.linyin.pojo.TimeLimited;

/**
 * TMR - Tell my requirement
 *
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tmr")
public class TmrData extends CachedPojo implements TimeLimited {
    private static final long serialVersionUID = 252607368970551027L;

    /**
     * Owner of this requirement.
     */
    @Id
    private Long accountId;

    /**
     * List of the user's requirement tags.
     */
    private List<Long> tags;

    private Date startTime;

    private Date endTime;

    /**
     * Additional message of the requirement.
     */
    private String message;

    @Override
    public String getMemkey() {
        return super.getMemkey() + accountId;
    }
}
