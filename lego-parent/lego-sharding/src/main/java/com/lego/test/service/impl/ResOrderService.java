package com.lego.test.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lego.test.entity.ResOrder;
import com.lego.test.mapper.ResOrderMapper;
import com.lego.test.service.IResOrderService;

@Service
public class ResOrderService extends ServiceImpl<ResOrderMapper, ResOrder> implements IResOrderService {

}
