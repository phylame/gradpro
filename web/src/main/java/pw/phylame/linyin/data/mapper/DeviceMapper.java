/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.data.mapper;

import java.util.List;

import pw.phylame.linyin.domain.Device;

public interface DeviceMapper extends CommonMapper<Device, String> {
    List<Device> selectByAuthorId(long authorId);
}
