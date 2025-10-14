package com.example.webflux.service.facade;

import com.example.webflux.model.facade.FacadeAvailableModel;
import com.example.webflux.model.facade.FacadeHomeResponseDto;
import com.example.webflux.model.llmclient.LlmModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacadeServiceImpl implements FacadeService{

    @Override
    public Mono<FacadeHomeResponseDto> getFacadeHomeResponseDto() {
        return Mono.fromCallable(()->{
            List<FacadeAvailableModel> availableModelList = Arrays.stream(LlmModel.values())
                    .map(availableModel ->  new FacadeAvailableModel(availableModel.name(), availableModel.getCode())).toList();
            return new FacadeHomeResponseDto(availableModelList);
        });

//        return Mono.fromCallable(()->{
//            LlmModel[] values = LlmModel.values();
//            List<FacadeAvailableModel> availableModelList = new ArrayList<>();
//
//            for (int i = 0; i <= values.length; i++) {
//                LlmModel availableModel = values[i];
//                FacadeAvailableModel fam = new FacadeAvailableModel(availableModel.name(), availableModel.getCode());
//                availableModelList.add(fam);
//            }
//            return new FacadeHomeResponseDto(availableModelList);
//        });
    }
}
