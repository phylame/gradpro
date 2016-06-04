/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.controller.form;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.domain.Device;
import pw.phylame.linyin.util.DateUtils;

/**
 * @author Peng Wan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppForm extends AbstractForm {
    @NotBlank(message = "{app.new.error.name}")
    private String name;

    @NotBlank(message = "{app.new.error.description}")
    private String description;

    private int platformType;

    private Date createTime = DateUtils.now();

    public Device dumpToDevice(long vendorId) {
        Device device = new Device();
        device.setName(name);
        device.setAuthorId(vendorId);
        device.setPlatformType(platformType);
        device.setCreateTime(createTime);
        device.setDescription(description);
        return device;
    }
}
