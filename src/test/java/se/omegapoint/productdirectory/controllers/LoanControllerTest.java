package se.omegapoint.productdirectory.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.exceptions.GlobalExceptionHandler;
import se.omegapoint.productdirectory.exceptions.ResourceNotFoundException;
import se.omegapoint.productdirectory.services.LoanService;
import se.omegapoint.productdirectory.testutil.JsonTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@Import(GlobalExceptionHandler.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;


    @Test
    @DisplayName("Ska returnera en tom lista och status kod 200")
    void getAllShouldReturn200() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of());

        mockMvc.perform(get("/api/loan"))
                .andExpect(status().isOk());

        verify(loanService).getAllLoans();
    }

    @Test
    @DisplayName("Ska returnera Loan by id och statuskoden 200")
    void getByIdShouldReturn200() throws Exception {
        Integer id = 1;
        when(loanService.getLoanById(id)).thenReturn(mock(LoanResponseDTO.class));

        mockMvc.perform(get("/api/loan/{id}", id))
                .andExpect(status().isOk());

        verify(loanService).getLoanById(id);
    }

    @Test
    @DisplayName("Skapar loan och returnerar statuskod 201 om json stämmer")
    void createShouldReturn201WhenValidBody() throws Exception {
        when(loanService.addLoan(any()))
                .thenReturn(mock(LoanResponseDTO.class));

        mockMvc.perform(post("/api/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/loan-valid.json")))
                .andExpect(status().isCreated());

        verify(loanService).addLoan(any());
    }

    @Test
    @DisplayName("Uppdatera ska returnera status kod 200 när json är rätt format")
    void updateShouldReturn200WhenValidBody() throws Exception {
        Integer id = 2;
        when(loanService.updateLoan(eq(id), any()))
                .thenReturn(mock(LoanResponseDTO.class));

        mockMvc.perform(put("/api/loan/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/loan-valid.json")))
                .andExpect(status().isOk());

        verify(loanService).updateLoan(eq(id), any());
    }

    @Test
    @DisplayName("När lånet raderas returnera status kod 204")
    void deleteShouldReturn204WhenDeleted() throws Exception {
        Integer id = 3;

        mockMvc.perform(delete("/api/loan/{id}", id))
                .andExpect(status().isNoContent());

        verify(loanService).deleteLoan(id);
    }


    @Test
    @DisplayName("När create körs med invalid json body så returneras statuskod 400")
    void createShouldReturn400WhenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/loan-invalid.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed."))
                .andExpect(jsonPath("$.path").value("/api/loan"));

        verifyNoInteractions(loanService);
    }

    @Test
    @DisplayName("När PUT körs med invalid body så returneras status kod 400")
    void updateShouldReturn400WhenInvalidBody() throws Exception {
        Integer id = 4;

        mockMvc.perform(put("/api/loan/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/loan-invalid.json")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed."))
                .andExpect(jsonPath("$.path").value("/api/loan/" + id));

        verifyNoInteractions(loanService);
    }


    @Test
    @DisplayName("När GetById körs så kastas ResourceNotFound Exception om lånet med angivna id inte hittas")
    void getByIdShouldReturn404WhenServiceThrowsResourceNotFound() throws Exception {
        Integer id = 5;
        doThrow(new ResourceNotFoundException("Could not find loan with id: " + id))
                .when(loanService).getLoanById(id);

        mockMvc.perform(get("/api/loan/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/loan/" + id));

        verify(loanService).getLoanById(id);
    }

    @Test
    @DisplayName("PUT ska returnera 404 när metoden kastar ResourceNotFound exception")
    void updateShouldReturn404WhenServiceThrowsResourceNotFound() throws Exception {
        Integer id = 6;
        doThrow(new ResourceNotFoundException("Loan with id " + id + " not found"))
                .when(loanService).updateLoan(eq(id), any());

        mockMvc.perform(put("/api/loan/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/loan-valid.json")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/loan/" + id));

        verify(loanService).updateLoan(eq(id), any());
    }

    @Test
    @DisplayName("Delete ska returnera 404 när servicen kastar ResourceNotFound exception")
    void deleteShouldReturn404WhenServiceThrowsResourceNotFound() throws Exception {
        Integer id = 7;
        doThrow(new ResourceNotFoundException("Loan with id: " + id + " not found"))
                .when(loanService).deleteLoan(id);

        mockMvc.perform(delete("/api/loan/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/loan/" + id));

        verify(loanService).deleteLoan(id);
    }


    @Test
    @DisplayName("GetById ska returnera statuskod 500 när servicen kastar RuntimeException")
    void getByIdShouldReturn500WhenUnexpectedException() throws Exception {
        Integer id = 8;
        doThrow(new RuntimeException("Boom"))
                .when(loanService).getLoanById(id);

        mockMvc.perform(get("/api/loan/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."))
                .andExpect(jsonPath("$.path").value("/api/loan/" + id));

        verify(loanService).getLoanById(id);
    }
}
