package com.video.download.dao.impl;

import com.video.download.common.enums.SpiderTypeEnum;
import com.video.download.dao.IParserService;
import com.video.download.dao.repository.ParserRepository;
import com.video.download.domain.entity.DynamicParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author xiaom
 * @Date 2020/3/24 13:58
 * @Version 1.0.0
 * @Description <>
 **/
@Service
@Slf4j
public class ParserServiceImpl implements IParserService {

    @Resource
    private ParserRepository parserRepository;

    @Override
    public DynamicParser getConfig(SpiderTypeEnum typeEnum) {
        if(typeEnum == null){
            return null;
        }
        Optional<List<DynamicParser>> optionalDynamicParsers = parserRepository.findByConfigTypeAndSpiderType("config",typeEnum.getSpiderType());
        return optionalDynamicParsers.map(dynamicParsers -> dynamicParsers.get(0)).orElse(null);
    }

    @Override
    public List<DynamicParser> getParsers(SpiderTypeEnum typeEnum) {
        if(typeEnum == null){
            return null;
        }
        return parserRepository.findByConfigTypeAndSpiderType("parser",typeEnum.getSpiderType()).orElse(null);
    }
}
