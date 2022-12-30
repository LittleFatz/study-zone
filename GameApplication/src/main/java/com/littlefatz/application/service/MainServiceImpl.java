package com.littlefatz.application.service;

import com.littlefatz.biz.dto.GameInfoDTO;
import com.littlefatz.biz.service.GameDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService{

    @Autowired
    private GameDemoService gameDemoService;

    @Override
    public String getDetail() {
        GameInfoDTO detail = gameDemoService.getDetail(1);
        System.out.println(detail.getName());
        System.out.println("application:   working");
        return detail.getName();
    }
}
