package com.lego.test.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lego.test.entity.ResLog;
import com.lego.test.mapper.ResLogMapper;
import com.lego.test.service.IResLogService;

@Service
public class ResLogService extends ServiceImpl<ResLogMapper, ResLog> implements IResLogService {

}
