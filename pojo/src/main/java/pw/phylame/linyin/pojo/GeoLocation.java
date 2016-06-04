/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Peng Wan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation implements java.io.Serializable {
    private static final long serialVersionUID = 7694544389596368319L;

    private double longitude;
    private char lngSuffix;
    private double latitude;
    private char latSuffix;

    /**
     * Gets current location by GPS support device.
     *
     * @return the location data
     */
    public static GeoLocation currentLocation() {
        // now, we get a random location
        return randomLocation();
    }

    /**
     * Makes a random location.
     *
     * @return the location data
     */
    public static GeoLocation randomLocation() {
        GeoLocation gl = new GeoLocation();
        gl.setLongitude(Math.random() * 180);
        gl.setLngSuffix(Math.random() >= 0.5 ? 'E' : 'W');
        gl.setLatitude(Math.random() * 90);
        gl.setLatSuffix(Math.random() >= 0.5 ? 'N' : 'S');
        return gl;
    }
}
