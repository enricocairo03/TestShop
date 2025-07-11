package com.testshop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testshop.webapp.services.ProdottiService;

@RestController
public class CachingController
{
    @Autowired
    private ProdottiService prodottiService;

    @GetMapping("claerAllCaches")
    public void clearAllCaches()
    {
        this.prodottiService.CleanCaches();
    }
}
