package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.exception.DuplicateResourceException;
import com.escuelajavag4.catalogservice.exception.ResourceHasDependenciesException;
import com.escuelajavag4.catalogservice.exception.ResourceNotFoundException;
import com.escuelajavag4.catalogservice.mapper.MarcaMapper;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.model.entity.Marca;
import com.escuelajavag4.catalogservice.model.entity.Product;
import com.escuelajavag4.catalogservice.repository.MarcaRepository;
import com.escuelajavag4.catalogservice.service.impl.MarcaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarcaServiceImplTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private MarcaMapper marcaMapper;

    @InjectMocks
    private MarcaServiceImpl marcaService;

    private Marca marca;
    private MarcaResponseDto responseDto;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setId(1L);
        marca.setName("Samsung");
        marca.setActive(true);
        marca.setProducts(new HashSet<>());

        responseDto = new MarcaResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Samsung");
        responseDto.setActive(true);
    }


    @Test
    void createMarca_ok() {
        MarcaCreateRequestDto dto = new MarcaCreateRequestDto();
        dto.setName("Samsung");

        when(marcaRepository.existsByNameIgnoreCase("Samsung")).thenReturn(false);
        when(marcaMapper.toEntity(dto)).thenReturn(marca);
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        MarcaResponseDto result = marcaService.createMarca(dto);

        assertNotNull(result);
        assertEquals("Samsung", result.getName());
        verify(marcaRepository).save(marca);
    }

    @Test
    void createMarca_duplicate_throwsException() {
        MarcaCreateRequestDto dto = new MarcaCreateRequestDto();
        dto.setName("Samsung");

        when(marcaRepository.existsByNameIgnoreCase("Samsung")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> marcaService.createMarca(dto));
        verify(marcaRepository, never()).save(any());
    }

    @Test
    void getAllMarcas_returnsList() {
        when(marcaRepository.findAll()).thenReturn(List.of(marca));
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        List<MarcaResponseDto> result = marcaService.getAllMarcas();

        assertEquals(1, result.size());
        assertEquals("Samsung", result.get(0).getName());
    }

    @Test
    void getAllMarcas_returnsEmptyList() {
        when(marcaRepository.findAll()).thenReturn(Collections.emptyList());

        List<MarcaResponseDto> result = marcaService.getAllMarcas();

        assertTrue(result.isEmpty());
    }


    @Test
    void getMarcaById_ok() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        MarcaResponseDto result = marcaService.getMarcaById(1L);

        assertEquals("Samsung", result.getName());
    }

    @Test
    void getMarcaById_notFound_throwsException() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> marcaService.getMarcaById(1L));
    }


    @Test
    void getMarcaByName_ok() {
        when(marcaRepository.findByNameIgnoreCase("Samsung")).thenReturn(Optional.of(marca));
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        MarcaResponseDto result = marcaService.getMarcaByName("Samsung");

        assertEquals("Samsung", result.getName());
    }

    @Test
    void getMarcaByName_notFound_throwsException() {
        when(marcaRepository.findByNameIgnoreCase("Samsung")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> marcaService.getMarcaByName("Samsung"));
    }


    @Test
    void getActiveMarcas_returnsList() {
        when(marcaRepository.findByActiveTrue()).thenReturn(List.of(marca));
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        List<MarcaResponseDto> result = marcaService.getActiveMarcas();

        assertEquals(1, result.size());
    }

    @Test
    void getActiveMarcas_returnsEmptyList() {
        when(marcaRepository.findByActiveTrue()).thenReturn(Collections.emptyList());

        List<MarcaResponseDto> result = marcaService.getActiveMarcas();

        assertTrue(result.isEmpty());
    }


    @Test
    void updateMarca_ok_sameName() {
        MarcaUpdateRequestDto dto = new MarcaUpdateRequestDto();
        dto.setName("Samsung");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        MarcaResponseDto result = marcaService.updateMarca(1L, dto);

        assertEquals("Samsung", result.getName());
    }

    @Test
    void updateMarca_ok_changeName_noDuplicate() {
        MarcaUpdateRequestDto dto = new MarcaUpdateRequestDto();
        dto.setName("Apple");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.existsByNameIgnoreCase("Apple")).thenReturn(false);
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        MarcaResponseDto result = marcaService.updateMarca(1L, dto);

        assertNotNull(result);
    }

    @Test
    void updateMarca_changeName_duplicate_throwsException() {
        MarcaUpdateRequestDto dto = new MarcaUpdateRequestDto();
        dto.setName("Apple");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.existsByNameIgnoreCase("Apple")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> marcaService.updateMarca(1L, dto));
    }

    @Test
    void updateMarca_notFound_throwsException() {
        MarcaUpdateRequestDto dto = new MarcaUpdateRequestDto();
        dto.setName("Apple");

        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> marcaService.updateMarca(1L, dto));
    }


    @Test
    void activateMarca_ok() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(marcaMapper.toResponseDto(marca)).thenReturn(responseDto);

        MarcaResponseDto result = marcaService.activateMarca(1L);

        assertTrue(result.getActive());
    }

    @Test
    void activateMarca_notFound_throwsException() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> marcaService.activateMarca(1L));
    }


    @Test
    void deactivateMarca_ok_noProducts() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        marcaService.deactivateMarca(1L);

        verify(marcaRepository).save(marca);
        assertFalse(marca.getActive());
    }

    @Test
    void deactivateMarca_withProducts_throwsException() {
        marca.getProducts().add(new Product());
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        assertThrows(ResourceHasDependenciesException.class, () -> marcaService.deactivateMarca(1L));
    }

    @Test
    void deactivateMarca_notFound_throwsException() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> marcaService.deactivateMarca(1L));
    }


    @Test
    void deleteMarca_ok_noProducts() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        marcaService.deleteMarca(1L);

        verify(marcaRepository).deleteById(1L);
    }

    @Test
    void deleteMarca_withProducts_throwsException() {
        marca.getProducts().add(new Product());
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        assertThrows(ResourceHasDependenciesException.class, () -> marcaService.deleteMarca(1L));
    }

    @Test
    void deleteMarca_notFound_throwsException() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> marcaService.deleteMarca(1L));
    }
}