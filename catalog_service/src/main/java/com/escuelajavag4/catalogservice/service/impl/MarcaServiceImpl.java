package com.escuelajavag4.catalogservice.service.impl;

import com.escuelajavag4.catalogservice.exception.DuplicateResourceException;
import com.escuelajavag4.catalogservice.exception.ResourceHasDependenciesException;
import com.escuelajavag4.catalogservice.exception.ResourceNotFoundException;
import com.escuelajavag4.catalogservice.mapper.MarcaMapper;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Marca;
import com.escuelajavag4.catalogservice.repository.MarcaRepository;
import com.escuelajavag4.catalogservice.service.MarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    @Override
    public MarcaResponseDto createMarca(MarcaCreateRequestDto createRequestDto) {
        if (marcaRepository.existsByNameIgnoreCase(createRequestDto.getName())) {
            throw new DuplicateResourceException("Marca", "nombre", createRequestDto.getName());
        }

        Marca marca = marcaMapper.toEntity(createRequestDto);
        Marca savedMarca = marcaRepository.save(marca);
        return marcaMapper.toResponseDto(savedMarca);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDto> getAllMarcas() {
        List<Marca> marcas = marcaRepository.findAll();
        return marcas.stream()
                .map(marcaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaResponseDto getMarcaById(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", id));
        return marcaMapper.toResponseDto(marca);
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaResponseDto getMarcaByName(String name) {
        Marca marca = marcaRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", name));
        return marcaMapper.toResponseDto(marca);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDto> getActiveMarcas() {
        List<Marca> activeMarcas = marcaRepository.findByActiveTrue();
        return activeMarcas.stream()
                .map(marcaMapper::toResponseDto)
                .toList();
    }

    @Override
    public MarcaResponseDto updateMarca(Long id, MarcaUpdateRequestDto updateRequestDto) {
        Marca existingMarca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", id));

        if (updateRequestDto.getName() != null &&
                !updateRequestDto.getName().equalsIgnoreCase(existingMarca.getName()) &&
                marcaRepository.existsByNameIgnoreCase(updateRequestDto.getName())) {
            throw new DuplicateResourceException("Marca", "nombre", updateRequestDto.getName());
        }

        marcaMapper.updateEntityFromDto(updateRequestDto, existingMarca);
        Marca updatedMarca = marcaRepository.save(existingMarca);
        return marcaMapper.toResponseDto(updatedMarca);
    }

    @Override
    public MarcaResponseDto activateMarca(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", id));

        marca.setActive(true);
        Marca activatedMarca = marcaRepository.save(marca);
        return marcaMapper.toResponseDto(activatedMarca);
    }

    @Override
    public void deactivateMarca(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", id));

        if(!marca.getProducts().isEmpty()) {
            throw new ResourceHasDependenciesException("Marca", "productos");
        }

        marca.setActive(false);
        marcaRepository.save(marca);
    }

    @Override
    public void deleteMarca(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", id));

        if (!marca.getProducts().isEmpty()) {
            throw new ResourceHasDependenciesException("Marca", "productos");
        }

        marcaRepository.deleteById(id);
    }
}