package com.example.webflux.model.facade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacadeHomeResponseDto {

    private List<FacadeAvailableModel> availableModelList;
}
