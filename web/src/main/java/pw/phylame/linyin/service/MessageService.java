/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */
package pw.phylame.linyin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pw.phylame.linyin.data.repository.MessageRepository;
import pw.phylame.linyin.pojo.Message;
import pw.phylame.linyin.ui.Pager;
import pw.phylame.linyin.ui.SpringPageAdapter;
import pw.phylame.linyin.util.MiscUtils;

/**
 * @author Peng Wan
 */
@Service
public class MessageService extends CommonService {
	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MqttSender mqttSender;

	@Autowired
	private UserService userService;

	@Autowired
	private MerchantService merchantService;

	public boolean publishMessage(Message message) {
		messageRepository.insert(message);
		// TODO: check the startTime
		String topic = String.format("lps/%d/%d", message.getVendor(), message.getTag());
		Map<String, Object> map = MiscUtils.toMap(message);
		map.put("vendor_id", message.getVendor());
		map.put("vendor", userService.getUserById(message.getVendor()).getName());
		map.put("tag_id", message.getTag());
		map.put("tag", merchantService.getTagById(message.getTag()).getName());
		return mqttSender.publish(topic, map);
	}

	public List<Message> getAllMessages() {
		return messageRepository.findAll();
	}

	public Pager getAllMessages(int page, int size) {
		return new SpringPageAdapter<>(messageRepository.findAll(new PageRequest(page - 1, size)));
	}

	public Message getMessageById(String messageId) {
		return messageRepository.findOne(messageId);
	}

	public int getMessageNumberByTag(long tagId) {
		return messageRepository.countByTag(tagId);
	}

	public List<Message> getMessagesByTag(long tagId) {
		return messageRepository.findByTag(tagId);
	}

	public Pager getMessagesByTag(long tagId, int page, int size) {
		return new SpringPageAdapter<>(messageRepository.findByTag(tagId, new PageRequest(page - 1, size)));
	}

	public int getMessageNumberByVendor(long vendorId) {
		return messageRepository.countByVendor(vendorId);
	}

	public List<Message> getMessagesByVendor(long vendorId) {
		return messageRepository.findByVendor(vendorId);
	}

	public Pager getMessagesByVendor(long accountId, int page, int size) {
		return new SpringPageAdapter<>(messageRepository.findByVendor(accountId, new PageRequest(page - 1, size)));
	}

	public List<Message> getMessagesByTagAndVendor(long tagId, long vendorId) {
		return messageRepository.findByTagAndVendor(tagId, vendorId);
	}

	public Pager getMessagesByTagAndVendor(long tagId, long vendorId, int page, int size) {
		return new SpringPageAdapter<>(
				messageRepository.findByTagAndVendor(tagId, vendorId, new PageRequest(page - 1, size)));
	}

	public void deleteMessage(String messageId) {
		// TODO: stop push message with mqtt
		messageRepository.delete(messageId);
	}
}
