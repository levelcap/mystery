package com.brave.mystery.controller;

import com.brave.mystery.model.Item;
import com.brave.mystery.repository.ItemRepository;
import com.brave.mystery.repository.UserRepository;
import com.brave.mystery.services.OrderService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dcohen on 2/11/15.
 */

@RestController
@RequestMapping("/api/item")
public class ItemController extends BaseController {
	@Autowired
	private ItemRepository itemRepository;

    @Autowired
    private OrderService orderService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<List<Item>> getItems(
			@RequestParam(value = "available", required = false) Boolean available) {
		if (null == available) {
			return new ResponseEntity<List<Item>>(itemRepository.findAll(),
					HttpStatus.OK);
		} else if (available) {
			return new ResponseEntity<List<Item>>(
					itemRepository.findByAvailableTrue(), HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Item>>(
					itemRepository.findByAvailableFalse(), HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<Item> getItem(@PathVariable("id") String id) {
		return new ResponseEntity<Item>(itemRepository.findOne(id),
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public HttpEntity<List<Item>> saveOrder(@RequestBody List<Item> items) {
		if (isLoggedIn()) {
			for (Item item : items) {
				if (item.getId() == null) {
					item.setId(new ObjectId().toString());
				}
			}
			itemRepository.save(items);
            orderService.updateItemsForOrdersToday(items);
			return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Item>>(HttpStatus.UNAUTHORIZED);
		}
	}
}
