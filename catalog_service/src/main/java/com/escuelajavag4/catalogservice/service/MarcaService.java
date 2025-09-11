package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;

import java.util.List;

public interface MarcaService {

    // Crear una marca
    MarcaResponseDto createMarca(MarcaCreateRequestDto createRequestDto);

    // Obtener todas las marcas
    List<MarcaResponseDto> getAllMarcas();

    // Obtener una marca por ID
    MarcaResponseDto getMarcaById(Long id);

    // Obtener una marca por nombre
    MarcaResponseDto getMarcaByName(String name);

    // Obtener marcas activas
    List<MarcaResponseDto> getActiveMarcas();

    // Actualizar una marca
    MarcaResponseDto updateMarca(Long id, MarcaUpdateRequestDto updateRequestDto);

    // Activar una marca
    MarcaResponseDto activateMarca(Long id);

    // Desactivar una marca
    void deactivateMarca(Long id);

    // Eliminar una marca
    void deleteMarca(Long id);
}
