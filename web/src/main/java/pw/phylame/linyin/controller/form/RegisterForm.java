/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.phylame.linyin.controller.ValidatorPattern;
import pw.phylame.linyin.domain.Account;
import pw.phylame.linyin.pojo.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterForm extends AbstractForm {
    @Pattern(regexp = ValidatorPattern.USERNAME, message = "{user.register.error.username}")
    private String username;

    @Pattern(regexp = ValidatorPattern.PASSWORD, message = "{user.register.error.password}")
    private String password;

    @Pattern(regexp = ValidatorPattern.PASSWORD, message = "{user.register.error.confirm}")
    private String confirm;

    @NotBlank(message = "{user.register.error.name}")
    private String name;

    @Pattern(regexp = ValidatorPattern.TELEPHONE, message = "{user.register.error.telephone}")
    private String telephone;

    private String intro;

    public boolean checkConfirm() {
        return password.equals(confirm);
    }

    public Account dumpToAccount() {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return account;
    }

    public User dumpToUser() {
        User user = new User();
        user.setName(name);
        user.setTelephone(telephone);
        user.setIntro(intro);
        return user;
    }
}
