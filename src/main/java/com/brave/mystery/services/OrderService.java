package com.brave.mystery.services;

import com.brave.mystery.model.Item;
import com.brave.mystery.model.ItemRequest;
import com.brave.mystery.model.Order;
import com.brave.mystery.model.User;
import com.brave.mystery.repository.ItemRepository;
import com.brave.mystery.repository.OrderRepository;
import com.brave.mystery.util.DateUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dcohen on 7/22/15.
 */
@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    public Order getBlankOrder(User user) {
        Order order = new Order();
        order.setId(new ObjectId().toString());
        order.setDate(DateUtil.getTodayDateString());
        order.setUserId(user.getId());
        order.setFilled(false);

        Set<ItemRequest> itemRequests = new HashSet<ItemRequest>();

        //populate all items
        List<Item> items = itemRepository.findByAvailableTrue();
        for (Item item : items) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setItem(item);
            itemRequest.setNumber(0);
            itemRequest.setSizeType(null);
            itemRequests.add(itemRequest);
        }

        order.setRequestedItems(itemRequests);

        return order;
    }

    public void updateItemsForOrdersToday(List<Item> items) {
        List<Order> orders = orderRepository.findByDate(DateUtil.getTodayDateString());

        for (Order order : orders) {
            Set<ItemRequest> itemRequests = order.getRequestedItems();
            for (Item item : items) {
                boolean exists = false;

                for (ItemRequest itemRequest : itemRequests) {
                    if (itemRequest.getItem().equals(item)) {
                        exists = true;
                        //Item match is by ID, update with the new item information
                        itemRequest.setItem(item);
                    }
                }

                if (!exists) {
                    ItemRequest itemRequest = new ItemRequest();
                    itemRequest.setItem(item);
                    itemRequest.setNumber(0);
                    itemRequest.setSizeType(null);
                    itemRequests.add(itemRequest);
                }
            }
            order.setRequestedItems(itemRequests);
        }

        orderRepository.save(orders);
    }

}
