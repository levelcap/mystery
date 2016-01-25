package com.brave.mystery.controller;

import com.brave.mystery.model.Character;
import com.brave.mystery.repository.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private CharacterRepository characterRepository;

    @RequestMapping("/")
    public String index(Model model) {
        try {

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "index";
    }

    @RequestMapping("/obstacle")
    public String obstacleTest(Model model) {
        try {

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "obstacle";
    }

    @RequestMapping("/map")
    public String mapPage(Model model) {
        try {

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "map";
    }

    @RequestMapping("/roster")
    public String charList(Model model) {
        try {
            List<Character> characters = characterRepository.findByNpcFalse();
            model.addAttribute("characters", characters);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "roster";
    }


    @RequestMapping("/npc")
    public String npcList(Model model) {
        try {
            List<Character> npcs = characterRepository.findByNpcTrue();
            model.addAttribute("npcs", npcs);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "npc";
    }

    @RequestMapping("/npc/{id}")
    public String npcPage(@PathVariable(value = "id") String id, Model model) {
        try {
            Character character = characterRepository.findOne(id);
            model.addAttribute("character", character);
            model.addAttribute("title", character.getName());

            return "npcPage";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "index";
    }

}
