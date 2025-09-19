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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<MarcaResponseDto> response = marcaController.createMarca(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nike", response.getBody().getName());

        verify(marcaService).createMarca(request);
    }

    @Test
    void testGetAllMarcas() {
        when(marcaService.getAllMarcas()).thenReturn(List.of(sampleMarca));

        ResponseEntity<List<MarcaResponseDto>> response = marcaController.getAllMarcas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(marcaService).getAllMarcas();
    }

    @Test
    void testGetMarcaById() {
        when(marcaService.getMarcaById(1L)).thenReturn(sampleMarca);

        ResponseEntity<MarcaResponseDto> response = marcaController.getMarcaById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(marcaService).getMarcaById(1L);
    }

    @Test
    void testGetMarcaByName() {
        when(marcaService.getMarcaByName("Nike")).thenReturn(sampleMarca);

        ResponseEntity<MarcaResponseDto> response = marcaController.getMarcaByName("Nike");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nike", response.getBody().getName());

        verify(marcaService).getMarcaByName("Nike");
    }

    @Test
    void testGetActiveMarcas() {
        when(marcaService.getActiveMarcas()).thenReturn(List.of(sampleMarca));

        ResponseEntity<List<MarcaResponseDto>> response = marcaController.getActiveMarcas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get(0).getActive());

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

        ResponseEntity<MarcaResponseDto> response = marcaController.updateMarca(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Adidas", response.getBody().getName());

        verify(marcaService).updateMarca(1L, updateRequest);
    }

    @Test
    void testActivateMarca() {
        when(marcaService.activateMarca(1L)).thenReturn(sampleMarca);

        ResponseEntity<MarcaResponseDto> response = marcaController.activateMarca(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getActive());

        verify(marcaService).activateMarca(1L);
    }

    @Test
    void testDeactivateMarca() {
        doNothing().when(marcaService).deactivateMarca(1L);

        ResponseEntity<Void> response = marcaController.deactivateMarca(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(marcaService).deactivateMarca(1L);
    }

    @Test
    void testDeleteMarca() {
        doNothing().when(marcaService).deleteMarca(1L);

        ResponseEntity<Void> response = marcaController.deleteMarca(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(marcaService).deleteMarca(1L);
    }
}
