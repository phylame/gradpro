/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import pw.phylame.linyin.domain.LoginState;

/**
 * @author Peng Wan
 */
@Component("loginStateRepository")
public interface LoginStateRepository extends MongoRepository<LoginState, Long> {
}
