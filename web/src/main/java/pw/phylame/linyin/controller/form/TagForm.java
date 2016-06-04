/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.controller.form;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Peng Wan
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagForm extends AbstractForm {
    @NotBlank(message = "{tag.new.error.name}")
    private String name;

    @NotBlank(message = "{tag.new.error.intro}")
    private String intro;
}
