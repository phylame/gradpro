/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.constants;

/**
 * Declares global error codes.
 *
 * @author Peng Wan
 */
public interface ErrorCode {
    int OK = 0;

    /**
     * The client with ID is not registered.
     */
    int CLIENT_NOT_AVAILABLE = -100_000;

    /**
     * The server occurs error(s).
     */
    int SERVER_ERROR = -100_001;

    int NO_JSON_DATA = -100_002;

    int BAD_JSON_DATA = -100_003;

    int NO_JSON_TYPE = -100_004;

    int NO_REQUIREMENT_TAGS = -100_005;

    int NO_SUCH_ACCOUNT = -100_006;

    int NOT_LOGGED_IN = -100_007;

    int REQUIREMENT_NOT_LIVING = -100_008;

    int USER_NOT_SUBSCRIBED = -100_009;

    int NO_NEARBY_MERCHANT = -100_011;

    int NO_MATCHED_MERCHANT = -100_012;

    int ACCOUNT_EXPIRED = -100_013;

    int BAD_ACCOUNT_ID = -100_014;

    int NETWORK_ERROR = -100_015;
}
