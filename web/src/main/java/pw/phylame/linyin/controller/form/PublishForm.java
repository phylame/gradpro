/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller.form;

import java.util.Date;

import javax.validation.constraints.Future;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.constants.Constants;
import pw.phylame.linyin.pojo.Message;
import pw.phylame.linyin.pojo.TimeLimited;

/**
 * Publication form.
 *
 * @author Peng Wan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PublishForm extends AbstractForm implements TimeLimited {
    /**
     * Title of this publication message.
     */
    @NotBlank(message = "{pub.error.title}")
    private String title;

    /**
     * Tag ID of this message.
     */
    private long tag;

    /**
     * Start time of the message.
     */
    @Future(message = "{pub.error.startTime}")
    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    private Date startTime;

    /**
     * End time of the message.
     */
    @Future(message = "{pub.error.endTime}")
    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    private Date endTime;

    /**
     * Text of the message content.
     */
    @NotBlank(message = "{pub.error.content}")
    private String content;

    public boolean checkEndTime() {
        return endTime.after(startTime);
    }

    public Message dumpToMessage() {
        Message message = new Message();
        message.setTitle(title);
        message.setTag(tag);
        message.setStartTime(getStartTime());
        message.setEndTime(getEndTime());
        message.setContent(content);
        return message;
    }
}
