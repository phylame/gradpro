/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.constants;

/**
 * Declares names in HTTP request and response for storing data.
 *
 * @author Peng Wan
 */
public interface HttpConstants {
    String CONTENT_TYPE = "Content-Type";

    String CONTENT_LENGTH = "Content-Length";

    /**
     * Name in HTTP header for saving client id of device.
     */
    String CLIENT_ID_NAME = "LYClient-ID";

    /**
     * Name in HTTP header for holding account ID.
     */
    String ACCOUNT_ID_NAME = "LYAccount-ID";

    /**
     * Name in HTTP header for indicating type of JSON body.
     */
    String JSON_TYPE_NAME = "LYJson-Type";

    /**
     * Name of HTTP JSON body for TML data.
     */
    String JSON_TYPE_TML = "tml";

    /**
     * Name of HTTP JSON body for TMQ data.
     */
    String JSON_TYPE_TMR = "tmq";

    String MIME_JSON_TEXT = "application/json";

    String MIME_URL_FORM = "application/x-www-form-urlencoded";

    String MIME_MULTI_PART_FORM = "multipart/form-data";
}
