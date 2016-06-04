/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.pojo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Peng Wan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "message")
public class Message extends CachedPojo implements TimeLimited {
    private static final long serialVersionUID = 8219785272498208736L;

    @Id
    private String messageId;

    private String title;

    private long vendor;

    private long tag;

    private Date startTime;

    private Date endTime;

    private String content;

    @Override
    public String getMemkey() {
        return super.getMemkey() + messageId;
    }
}
