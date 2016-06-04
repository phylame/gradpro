/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut. Copyright
 * (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.phylame.linyin.data.mapper.DeviceMapper;
import pw.phylame.linyin.domain.Device;
import pw.phylame.linyin.ui.Pager;

@Service
public class DeviceService extends CommonService {
    private static final List<String> PLATFORMS = Arrays.asList("Android", "iOS", "WP");
    @Autowired
    private DeviceMapper deviceMapper;

    public Map<Integer, String> getAvailablePlatforms() {
        Map<Integer, String> platforms = new LinkedHashMap<>();
        PLATFORMS.forEach(name -> platforms.put(platforms.size() + 1, name));
        return platforms;
    }

    public void newApp(Device device) {
        device.setClientId(UUID.randomUUID().toString());
        device.setExpiredTime(null);
        deviceMapper.insert(device);
    }

    public Device getDeviceById(String clientId) {
        return deviceMapper.selectById(clientId);
    }

    public boolean isDeviceAvailable(String clientId) {
        Device device = deviceMapper.selectById(clientId);
        return device != null && !device.isExpired();
    }

    public Pager getAllApps(int page, int size) {
        return selectAll(deviceMapper, page, size);
    }

    public Pager getAppsByAuthor(long authorId, int page, int size) {
        return pagingQuery(() -> deviceMapper.selectByAuthorId(authorId), page, size);
    }
}
