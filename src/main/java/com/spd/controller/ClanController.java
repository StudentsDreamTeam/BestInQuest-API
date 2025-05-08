package com.spd.controller;

import com.spd.model.Clan;
import com.spd.service.ClanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clans")
public class ClanController {

    @Autowired
    private ClanService clanService;

    @PostMapping("/create")
    public Clan createClan(@RequestBody Clan clan, @RequestParam Long userId) {
        return clanService.createClan(clan, userId);
    }

    @GetMapping("/{id}")
    public Clan getClanInfo(@PathVariable Long id) {
        return clanService.getClanInfo(id);
    }
}
