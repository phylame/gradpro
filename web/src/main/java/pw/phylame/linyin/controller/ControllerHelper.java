/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.controller;

import static pw.phylame.linyin.util.MiscUtils.fillMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pw.phylame.linyin.pojo.User;
import pw.phylame.linyin.service.LocationService;
import pw.phylame.linyin.service.MerchantService;
import pw.phylame.linyin.service.MessageService;
import pw.phylame.linyin.service.SubscribeService;
import pw.phylame.linyin.service.UserService;

@Component
class ControllerHelper {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private SubscribeService subscribeService;

    Map<String, Object> prepareTag(Map<String, Object> propertyMap, User currentUser) {
        long creator = (long) propertyMap.get("creator"), tagId = (long) propertyMap.get("tagId");
        // for 0 is created by system
        propertyMap.put("creator", creator != 0 ? userService.getUserById(creator) : null);
        propertyMap.put("userNumber", userService.getUserNumberForTag(tagId));
        propertyMap.put("subNumber", subscribeService.subscriptionNumberForTag(tagId));
        propertyMap.put("msgNumber", messageService.getMessageNumberByTag(tagId));
        if (currentUser != null) {
            propertyMap.put("subscribed", subscribeService.isTagSubscribed(currentUser.getAccountId(), tagId));
        }
        return propertyMap;
    }

    <I extends Iterable<Map<String, Object>>> I prepareTags(I iter, User currentUser) {
        iter.forEach(map -> prepareTag(map, currentUser));
        return iter;
    }

    Map<String, Object> prepareUser(Map<String, Object> propertyMap, User currentUser) {
        long accountId = (long) propertyMap.get("accountId"), locationId = (long) propertyMap.get("locationId");
        propertyMap.put("location", locationService.getLocationById(locationId));
        propertyMap.put("tags", merchantService.getTagsByVendor(accountId));
        propertyMap.put("subNumber", subscribeService.subscriptionNumberForMerchant(accountId));
        propertyMap.put("msgNumber", messageService.getMessageNumberByVendor(accountId));
        if (currentUser != null && currentUser.getAccountId() != accountId) {
            propertyMap.put("subscribed", subscribeService.isMerchantSubscribed(currentUser.getAccountId(), accountId));
        }
        return propertyMap;
    }

    <I extends Iterable<Map<String, Object>>> I prepareUsers(I iter, User currentUser) {
        iter.forEach(map -> prepareUser(map, currentUser));
        return iter;
    }

    <I extends Iterable<Map<String, Object>>> I prepareSubscribers(I iter, User currentUser) {
        iter.forEach(map -> {
            // make map look like user
            fillMap(map, userService.getUserById((long) map.get("accountId")));
            prepareUser(map, currentUser);
        });
        return iter;
    }

    Map<String, Object> prepareMessage(Map<String, Object> propertyMap) {
        propertyMap.put("tag", merchantService.getTagById((long) propertyMap.get("tag")));
        propertyMap.put("vendor", userService.getUserById((long) propertyMap.get("vendor")));
        return propertyMap;
    }

    <I extends Iterable<Map<String, Object>>> I prepareMessages(I iter, User currentUser) {
        iter.forEach(this::prepareMessage);
        return iter;
    }

    Map<String, Object> prepareApp(Map<String, Object> propertyMap) {
        propertyMap.put("author", userService.getUserById((long) propertyMap.get("authorId")));
        return propertyMap;
    }

    <I extends Iterable<Map<String, Object>>> I prepareApps(I iter, User currentUser) {
        iter.forEach(this::prepareApp);
        return iter;
    }
}
