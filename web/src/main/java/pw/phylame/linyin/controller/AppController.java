/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pw.phylame.linyin.constants.AccessType;
import pw.phylame.linyin.controller.form.AppForm;
import pw.phylame.linyin.domain.Device;
import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.service.DeviceService;
import pw.phylame.linyin.service.UserService;
import pw.phylame.linyin.ui.EmptyPager;
import pw.phylame.linyin.ui.Pager;

/**
 * @author Peng Wan
 *
 */
@Controller
@RequestMapping(value = "/ocs/app")
public class AppController extends BaseController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerHelper helper;

    private String forNew(AppForm form) {
        form.setType(AccessType.WEB);
        form.setPlatformType(1);
        Map<String, String> platforms = new LinkedHashMap<>();
        deviceService.getAvailablePlatforms().forEach((type, name) -> platforms.put(Integer.toString(type), name));
        model.addAttribute("platforms", platforms);
        return Pages.APP_NEW;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNewApp(@ModelAttribute("appForm") AppForm form, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performOrLogin(user -> forNew(form));
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String processNewApp(@Valid @ModelAttribute("appForm") AppForm form, BindingResult br,
            HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performOrLogin(user -> {
            if (!validateSubmit(form, br)) {
                return forNew(form);
            }
            Device device = form.dumpToDevice(user.getAccountId());
            deviceService.newApp(device);
            return redirectToSourceOr(Pages.appDetail(device.getClientId()));
        });
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.APP_LIST, this::apps);
    }

    @RequestMapping(value = "/list/author/{authorId}", method = RequestMethod.GET)
    public String listForAuthor(@PathVariable("authorId") String authorId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        return performList(Pages.APP_LIST, (page, size) -> appsForAuthor(authorId, page, size));
    }

    @RequestMapping(value = "/detail/{clientId}")
    public String app(@PathVariable("clientId") String clientId, HttpServletRequest req, Model m) {
        handleRequest(req, m);
        Device device = deviceService.getDeviceById(clientId);
        if (device == null) {
            return performText("no such app with id: {0}", clientId);
        }
        model.addAttribute("app", device);
        return Pages.APP_DETAIL;
    }

    private Pager apps(int page, int size) {
        setPageTitle("全部 App");
        return helper.prepareApps(deviceService.getAllApps(page, size), getLoggedUser());
    }

    private Pager appsForAuthor(String authorId, int page, int size) {
        return prepareValue(authorId, Long::parseLong, id -> {
            User user = userService.getUserById(id);
            if (user == null) {
                return new EmptyPager(size);
            }
            User currentUser = getLoggedUser();
            setPageTitle("{0}的 App", myNameOr(user, currentUser));
            model.addAttribute("for_author", 1);
            return helper.prepareApps(deviceService.getAppsByAuthor(id, page, size), currentUser);
        }, (id, e) -> new EmptyPager(size));
    }
}
