/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import org.springframework.stereotype.Service;

import pw.phylame.linyin.pojo.GeoLocation;

/**
 * Created by Nanu on 2016-4-28.
 */
@Service
public class LocationService extends CommonService {
    public GeoLocation getLocationById(long locationId) {
        if (locationId == 0) {
            return null;
        }
        return GeoLocation.currentLocation();
    }
}
