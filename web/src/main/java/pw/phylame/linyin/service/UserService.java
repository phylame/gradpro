/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import static pw.phylame.linyin.util.StringUtils.md5OfString;
import static pw.phylame.linyin.util.StringUtils.notEmptyOr;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import pw.phylame.linyin.constants.UserRoles;
import pw.phylame.linyin.data.mapper.AccountMapper;
import pw.phylame.linyin.data.mapper.AutoLoginMapper;
import pw.phylame.linyin.data.mapper.LoginLogMapper;
import pw.phylame.linyin.data.mapper.RegisterLogMapper;
import pw.phylame.linyin.data.mapper.UserLogMapper;
import pw.phylame.linyin.data.mapper.UserMapper;
import pw.phylame.linyin.data.repository.LoginStateRepository;
import pw.phylame.linyin.domain.Account;
import pw.phylame.linyin.domain.AutoLogin;
import pw.phylame.linyin.domain.LoginLog;
import pw.phylame.linyin.domain.LoginState;
import pw.phylame.linyin.domain.RegisterLog;
import pw.phylame.linyin.domain.UserLog;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.ui.PageHelperAdapter;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.util.DateUtils;
import pw.phylame.linyin.util.MiscUtils;

@Service("userService")
public class UserService extends CommonService {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AutoLoginMapper autoLoginMapper;

    @Autowired
    private RegisterLogMapper registerLogMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private UserLogMapper userLogMapper;

    @Autowired
    private LoginStateRepository loginRepository;

    public boolean isAccountExist(long accountId) {
        return accountMapper.selectById(accountId) != null;
    }

    public boolean isAccountExist(String username) {
        return accountMapper.selectByUsername(username) != null;
    }

    private <R> R forLoginItem(long accountId, String loginType, String clientId,
                               BiFunction<LoginState, LoginState.Item, R> task) {
        LoginState state = loginRepository.findOne(accountId);
        if (state == null || MiscUtils.isEmpty(state.getRecords())) {
            return task.apply(state, null);
        }
        LoginState.Item item = MiscUtils.find(state.getRecords(),
                i -> i.getType().equals(loginType) && i.getClient().equals(clientId));
        return task.apply(state, item);
    }

    public boolean isAccountLoggedIn(long accountId, String loginType, String clientId) {
        return forLoginItem(accountId, loginType, clientId, (state, item) -> {
            if (state == null || item == null) {
                return false;
            } else if (item.isExpired()) {
                state.getRecords().remove(item);
                loginRepository.save(state);
                return false;
            }
            // set new expire time
            item.setExpiration(defaultLoginExpiredTime());
            loginRepository.save(state);
            return true;
        });
    }

    public Account getAccountById(long accountId) {
        return accountMapper.selectById(accountId);
    }

    public Account getAccountByUsernameAndPassword(String username, String password) {
        return accountMapper.selectByUsernameAndPassword(MiscUtils.asMap("username", username, "password", password));
    }

    /**
     * Registers standard user account from Web.
     */
    public void registerAccount(Account account, User user, String ip, String type, String clientId, String message) {
        Date now = DateUtils.now();
        account.setRole(DEFAULT_USER_ROLE);
        account.setExpiration(defaultAccountExpiredTime());
        accountMapper.insert(account);
        user.setAccountId(account.getAccountId());
        userMapper.insert(user);
        logRegister(account.getAccountId(), now, ip, type, clientId, message);
    }

    public void updateAccount(Account account, String ip, String type, String clientId, String message) {
        Date now = DateUtils.now();
        accountMapper.updateById(account);
        logModify(account.getAccountId(), now, ip, type, clientId, notEmptyOr(message, "update account settings"));
    }

    public boolean updatePassword(String username, String password, String ip, String type, String clientId,
                                  String message) {
        Account account = accountMapper.selectByUsername(username);
        if (account == null) {
            return false;
        }
        account.setPassword(password);
        updateAccount(account, ip, type, clientId, notEmptyOr(message, "reset password"));
        return true;
    }

    public void deleteAccount(long accountId, String ip, String type, String clientId, String message) {
        Date now = DateUtils.now();
        Account account = getAccountById(accountId);
        // just make the account expired
        account.setExpiration(now);
        accountMapper.updateById(account);
        logModify(accountId, now, ip, type, clientId, notEmptyOr(message, "delete account"));
    }

    public boolean loginAccount(Account account, String ip, String type, int tries, String clientId, String message) {
        if (account.isExpired()) {
            logger.debug("try login with expired account: " + account.getAccountId());
            return false;
        }
        return loginAccount(account.getAccountId(), ip, type, tries, clientId, message);
    }

    public boolean loginAccount(long accountId, String ip, String type, int tries, String clientId, String message) {
        Date now = DateUtils.now();
        return forLoginItem(accountId, type, clientId, (state, item) -> {
            if (item != null) {
                logger.debug("account with id {} already logged", accountId);
                return false;
            }
            if (state == null) {
                state = new LoginState();
                state.setAccountId(accountId);
            }
            if (MiscUtils.isEmpty(state.getRecords())) {
                state.setRecords(Arrays.asList(new LoginState.Item(type, clientId, defaultLoginExpiredTime())));
            } else {
                state.getRecords().add(new LoginState.Item(type, clientId, defaultLoginExpiredTime()));
            }
            loginRepository.save(state);
            logLogin(accountId, now, ip, type, tries, clientId, message);
            return true;
        });
    }

    public boolean logoutAccount(long accountId, String loginType, String clientId) {
        return forLoginItem(accountId, loginType, clientId, (state, item) -> {
            if (state == null || item == null) {
                return false;
            }
            logger.debug("remove account {} with type {} in client {} from login state db", accountId, loginType,
                    clientId);
            state.getRecords().remove(item);
            loginRepository.save(state);
            return true;
        });
    }

    public long getUserNumber() {
        return userMapper.count();
    }

    public User getUserById(long accountId) {
        return userMapper.selectById(accountId);
    }

    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    public List<User> getAllUsers(int limit) {
        if (limit > 0) {
            PageHelper.offsetPage(1, limit);
        }
        return userMapper.selectAll();
    }

    public Pager getAllUsers(int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(userMapper.selectAll());
    }

    public Pager getUsersForTag(long tagId, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(userMapper.selectByTagId(tagId));
    }

    public long getUserNumberForTag(long tagId) {
        return userMapper.countForTagId(tagId);
    }

    public List<User> getUsersByFuzzyName(String name) {
        return userMapper.selectByNameLike('%' + name + '%');
    }

    public Pager getUsersByFuzzyName(String name, int page, int size) {
        PageHelper.startPage(page, size);
        return new PageHelperAdapter<>(userMapper.selectByNameLike('%' + name + '%'));
    }

    public void updateUser(User user, String type, String ip, String clientId, String message) {
        Date now = DateUtils.now();
        userMapper.updateById(user);
        logModify(user.getAccountId(), now, ip, type, clientId, notEmptyOr(message, "update user details"));
    }

    public String updateAutoLogin(String username) {
        AutoLogin autoLogin = autoLoginMapper.selectByUsername(username);
        if (autoLogin == null) {
            autoLogin = new AutoLogin();
            autoLogin.setUsername(username);
            autoLogin.setToken(md5OfString(Long.toString(System.currentTimeMillis())));
        }
        autoLogin.setExpiration(defaultAutoLoginExpiredTime());
        autoLoginMapper.insertOrUpdate(autoLogin);
        return autoLogin.getToken();
    }

    public User tryAutoLogin(String username, String token, String ip, String type, String clientId, String message) {
        AutoLogin autoLogin = autoLoginMapper.selectByUsername(username);
        if (!autoLogin.getToken().equals(token)) {
            logger.debug("the token is invalid");
            return null;
        }
        if (autoLogin.isExpired()) {
            logger.debug("auto login state is expired");
            return null;
        }
        Account account = accountMapper.selectByUsername(username);
        if (account == null) {
            logger.error("not found account for username {}", username);
            return null;
        }
        User user = userMapper.selectById(account.getAccountId());
        loginAccount(account, ip, type, 1, clientId, message);
        return user;
    }

    private void logRegister(long accountId, Date time, String ip, String type, String clientId, String message) {
        RegisterLog log = new RegisterLog();
        log.setAccountId(accountId);
        log.setTime(time);
        log.setIp(ip);
        log.setType(type);
        log.setClientId(clientId);
        log.setMessage(message);
        registerLogMapper.insert(log);
    }

    public void logLogin(long accountId, Date time, String ip, String type, int tries, String clientId,
                         String message) {
        LoginLog log = new LoginLog();
        log.setAccountId(accountId);
        log.setTime(time);
        log.setIp(ip);
        log.setType(type);
        log.setTries(tries);
        log.setClientId(clientId);
        log.setMessage(message);
        loginLogMapper.insert(log);
    }

    private void logModify(long accountId, Date time, String ip, String type, String clientId, String message) {
        UserLog log = new UserLog();
        log.setAccountId(accountId);
        log.setTime(time);
        log.setIp(ip);
        log.setType(type);
        log.setClientId(clientId);
        log.setMessage(message);
        userLogMapper.insert(log);
    }

    private static final String DEFAULT_USER_ROLE = UserRoles.NORMAL;

    private static final int ACCOUNT_EXPIRED_YEARS = 2;

    // in minutes
    private static final int LOGIN_EXPIRED_MINUTES = 16;

    // save 7 days
    private static final int AUTO_LOGIN_EXPIRED_DAYS = 7;

    private static Date defaultAccountExpiredTime() {
        return DateUtils.calculateDateByNow('y', ACCOUNT_EXPIRED_YEARS);
    }

    private static Date defaultLoginExpiredTime() {
        return DateUtils.calculateDateByNow('n', LOGIN_EXPIRED_MINUTES);
    }

    private static Date defaultAutoLoginExpiredTime() {
        return DateUtils.calculateDateByNow('d', AUTO_LOGIN_EXPIRED_DAYS);
    }
}
