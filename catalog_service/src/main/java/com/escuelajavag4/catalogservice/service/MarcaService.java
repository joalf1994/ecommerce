package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;

import java.util.List;

public interface MarcaService {
    MarcaResponseDto createMarca(MarcaCreateRequestDto createRequestDto);

    List<MarcaResponseDto> getAllMarcas();

    MarcaResponseDto getMarcaById(Long id);

    MarcaResponseDto getMarcaByName(String name);

    List<MarcaResponseDto> getActiveMarcas();

    MarcaResponseDto updateMarca(Long id, MarcaUpdateRequestDto updateRequestDto);

    MarcaResponseDto activateMarca(Long id);

    void deactivateMarca(Long id);

    void deleteMarca(Long id);
}
