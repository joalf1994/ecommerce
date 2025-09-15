package com.escuelajavag4.catalogservice.controller;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.service.MarcaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MarcaControllerTest {

    @Mock
    private MarcaService marcaService;

    @InjectMocks
    private MarcaController marcaController;

    private MarcaResponseDto sampleMarca;

    @BeforeEach
    void setUp() {
        sampleMarca = new MarcaResponseDto();
        sampleMarca.setId(1L);
        sampleMarca.setName("Nike");
        sampleMarca.setActive(true);
    }

    @Test
    void testCreateMarca() {
        MarcaCreateRequestDto request = new MarcaCreateRequestDto();
        request.setName("Nike");

        when(marcaService.createMarca(request)).thenReturn(sampleMarca);

        MarcaResponseDto result = marcaController.createMarca(request);
        assertNotNull(result);
        assertEquals("Nike", result.getName());

        verify(marcaService).createMarca(request);
    }

    @Test
    void testGetAllMarcas() {
        when(marcaService.getAllMarcas()).thenReturn(List.of(sampleMarca));

        List<MarcaResponseDto> result = marcaController.getAllMarcas();
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(marcaService).getAllMarcas();
    }

    @Test
    void testGetMarcaById() {
        when(marcaService.getMarcaById(1L)).thenReturn(sampleMarca);

        MarcaResponseDto result = marcaController.getMarcaById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(marcaService).getMarcaById(1L);
    }

    @Test
    void testGetMarcaByName() {
        when(marcaService.getMarcaByName("Nike")).thenReturn(sampleMarca);

        MarcaResponseDto result = marcaController.getMarcaByName("Nike");
        assertNotNull(result);
        assertEquals("Nike", result.getName());

        verify(marcaService).getMarcaByName("Nike");
    }

    @Test
    void testGetActiveMarcas() {
        when(marcaService.getActiveMarcas()).thenReturn(List.of(sampleMarca));

        List<MarcaResponseDto> result = marcaController.getActiveMarcas();
        assertNotNull(result);
        assertTrue(result.get(0).getActive());

        verify(marcaService).getActiveMarcas();
    }

    @Test
    void testUpdateMarca() {
        MarcaUpdateRequestDto updateRequest = new MarcaUpdateRequestDto();
        updateRequest.setName("Adidas");

        MarcaResponseDto updatedMarca = new MarcaResponseDto();
        updatedMarca.setId(1L);
        updatedMarca.setName("Adidas");
        updatedMarca.setActive(true);

        when(marcaService.updateMarca(1L, updateRequest)).thenReturn(updatedMarca);

        MarcaResponseDto result = marcaController.updateMarca(1L, updateRequest);
        assertNotNull(result);
        assertEquals("Adidas", result.getName());

        verify(marcaService).updateMarca(1L, updateRequest);
    }

    @Test
    void testActivateMarca() {
        when(marcaService.activateMarca(1L)).thenReturn(sampleMarca);

        MarcaResponseDto result = marcaController.activateMarca(1L);
        assertNotNull(result);
        assertTrue(result.getActive());

        verify(marcaService).activateMarca(1L);
    }

    @Test
    void testDeactivateMarca() {
        doNothing().when(marcaService).deactivateMarca(1L);

        marcaController.deactivateMarca(1L);

        verify(marcaService).deactivateMarca(1L);
    }

    @Test
    void testDeleteMarca() {
        doNothing().when(marcaService).deleteMarca(1L);

        marcaController.deleteMarca(1L);

        verify(marcaService).deleteMarca(1L);
    }
}