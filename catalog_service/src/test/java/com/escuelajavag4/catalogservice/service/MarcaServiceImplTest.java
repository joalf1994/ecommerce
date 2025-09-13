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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarcaServiceImplTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private MarcaMapper marcaMapper;

    @InjectMocks
    private MarcaServiceImpl marcaService;

    private Marca marca;
    private MarcaResponseDto marcaDto;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setId(1L);
        marca.setName("MarcaX");
        marca.setActive(true);

        marcaDto = new MarcaResponseDto();
        marcaDto.setId(1L);
        marcaDto.setName("MarcaX");
        marcaDto.setActive(true);
    }

    @Test
    void crear_ok() {
        MarcaCreateRequestDto req = new MarcaCreateRequestDto();
        req.setName("MarcaX");

        when(marcaRepository.existsByNameIgnoreCase("MarcaX")).thenReturn(false);
        when(marcaMapper.toEntity(req)).thenReturn(marca);
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(marcaMapper.toResponseDto(marca)).thenReturn(marcaDto);

        MarcaResponseDto result = marcaService.createMarca(req);

        assertNotNull(result);
        assertEquals("MarcaX", result.getName());
        verify(marcaRepository).save(marca);
    }

    @Test
    void crear_nombreDuplicado() {
        MarcaCreateRequestDto req = new MarcaCreateRequestDto();
        req.setName("MarcaX");

        when(marcaRepository.existsByNameIgnoreCase("MarcaX")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> marcaService.createMarca(req));
    }

    @Test
    void buscarPorId_ok() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaMapper.toResponseDto(marca)).thenReturn(marcaDto);

        MarcaResponseDto result = marcaService.getMarcaById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_noExiste() {
        when(marcaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> marcaService.getMarcaById(99L));
    }

    @Test
    void actualizar_ok() {
        MarcaUpdateRequestDto req = new MarcaUpdateRequestDto();
        req.setName("MarcaY");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.existsByNameIgnoreCase("MarcaY")).thenReturn(false);
        doNothing().when(marcaMapper).updateEntityFromDto(req, marca);
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(marcaMapper.toResponseDto(marca)).thenReturn(marcaDto);

        MarcaResponseDto result = marcaService.updateMarca(1L, req);

        assertNotNull(result);
        verify(marcaRepository).save(marca);
    }

    @Test
    void eliminar_ok() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        doNothing().when(marcaRepository).deleteById(1L);

        marcaService.deleteMarca(1L);

        verify(marcaRepository).deleteById(1L);
    }

    @Test
    void eliminar_conProductos() {
        marca.getProducts().add(mock(Product.class));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        assertThrows(ResourceHasDependenciesException.class,
                () -> marcaService.deleteMarca(1L));
    }
}