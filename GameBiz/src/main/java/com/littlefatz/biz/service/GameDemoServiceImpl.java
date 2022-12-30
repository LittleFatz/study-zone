package com.littlefatz.biz.service;

import com.littlefatz.biz.dto.GameInfoDTO;
import com.littlefatz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameDemoServiceImpl implements GameDemoService {

    @Autowired
    private UserService userService;

    @Override
    public GameInfoDTO getDetail(int type) {

        int userType = userService.getUser();

        GameInfoDTO result = new GameInfoDTO();
        result.setId(userType);
        result.setName("done");
        System.out.println("biz:   working");
        return result;
    }
}
