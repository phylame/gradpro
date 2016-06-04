/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller.form;

import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.controller.ValidatorPattern;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginForm extends AbstractForm {
    @Pattern(regexp = ValidatorPattern.USERNAME, message = "{}")
    private String username;

    @Pattern(regexp = ValidatorPattern.PASSWORD, message = "{}")
    private String password;

    private boolean remember = false;
}
