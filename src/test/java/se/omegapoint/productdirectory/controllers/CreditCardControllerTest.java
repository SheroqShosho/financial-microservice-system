package se.omegapoint.productdirectory.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.exceptions.GlobalExceptionHandler;
import se.omegapoint.productdirectory.exceptions.ResourceNotFoundException;
import se.omegapoint.productdirectory.services.CreditCardService;
import se.omegapoint.productdirectory.testutil.JsonTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditCardController.class)
@Import(GlobalExceptionHandler.class)
class CreditCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreditCardService creditCardService;


    @Test
    @DisplayName("Ska returnera en tom lista och status kod 200")
    void getAllShouldReturn200() throws Exception {

        // Servicen kör getAllCreditCards metoden och returnerar en tom lista
        when(creditCardService.getAllCreditCards()).thenReturn(List.of());

        //MockMvc testar endpointen med angivna pathen, och returnerar statuskod OK
        mockMvc.perform(get("/api/creditcard"))
                .andExpect(status().isOk());

        //Verifierar att service metoden körs en gång
        verify(creditCardService).getAllCreditCards();
    }

    @Test
    @DisplayName("Ska returnera en CreditCard by id och statuskoden 200")
    void getByIdShouldReturn200() throws Exception {
        Integer id = 1;

        //Service metoden hämtar id och senare mockas som response DTO
        when(creditCardService.getCreditCardById(id)).thenReturn(mock(CreditCardResponseDTO.class));

        //MockMvc testar endpointen med angivna pathen genom id, och returnerar statuskod OK
        mockMvc.perform(get("/api/creditcard/{id}", id))
                .andExpect(status().isOk());

        //Verifierar att service metoden körs en gång
        verify(creditCardService).getCreditCardById(id);
    }

    @Test
    @DisplayName("Skapar kreditkort och returnerar statuskod 201 om json stämmer")
    void createShouldReturn201WhenValidBody() throws Exception {

        // Service metoden körs och returnerar en mock av response DTO
        when(creditCardService.addCreditCard(any()))
                .thenReturn(mock(CreditCardResponseDTO.class));

        //MockMvc testar endpointen och läser av json valid filen och returnerar statuskod 201 created
        mockMvc.perform(post("/api/creditcard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/creditcard-valid.json")))
                .andExpect(status().isCreated());

        //Verifierar att service metoden körs en gång
        verify(creditCardService).addCreditCard(any());
    }

    @Test
    @DisplayName("Uppdatera ska returnera status kod 200 när json är rätt format")
    void updateShouldReturn200WhenValidBody() throws Exception {
        Integer id = 2;
        when(creditCardService.updateCreditCard(eq(id), any()))
                .thenReturn(mock(CreditCardResponseDTO.class));

        mockMvc.perform(put("/api/creditcard/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/creditcard-valid.json")))
                .andExpect(status().isOk());

        verify(creditCardService).updateCreditCard(eq(id), any());
    }

    @Test
    @DisplayName("När kortet raderas returnera status kod 204")
    void deleteShouldReturn204WhenDeleted() throws Exception {
        Integer id = 3;

        mockMvc.perform(delete("/api/creditcard/{id}", id))
                .andExpect(status().isNoContent());

        verify(creditCardService).deleteCreditCard(id);
    }


    @Test
    @DisplayName("När create körs med invalid json body så returneras statuskod 400")
    void createShouldReturn400WhenInvalidJsonBody() throws Exception {
        mockMvc.perform(post("/api/creditcard")

                        //Säger att det är JSON-format som ska läsas
                        .contentType(MediaType.APPLICATION_JSON)

                        //Användning av readJson metoden för att läsa av invalid json filen och göra den till en sträng
                        .content(JsonTestUtils.readJson("json/creditcard-invalid.json")))

                //Förväntar sig rätt statuskod i detta fall BAD REQUEST
                .andExpect(status().isBadRequest())

                //Förväntar sig att statuskoden är 400
                .andExpect(jsonPath("$.status").value(400))

                //Förväntar sig att error msg är validation failed som hämtas från GlobalExceptionHandler
                .andExpect(jsonPath("$.error").value("Validation failed."))

                //Förväntar sig att pathen är rätt i detta fall /api/creditcard"
                .andExpect(jsonPath("$.path").value("/api/creditcard"));

        //Verifierar att servicen aldrig körs
        verifyNoInteractions(creditCardService);
    }

    @Test
    @DisplayName("När PUT körs med invalid body så returneras status kod 400")
    void updateShouldReturn400WhenInvalidBody() throws Exception {
        Integer id = 4;

        mockMvc.perform(put("/api/creditcard/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/creditcard-invalid.json")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed."))
                .andExpect(jsonPath("$.path").value("/api/creditcard/" + id));

        //Verifierar att servicen aldrig körs
        verifyNoInteractions(creditCardService);
    }


    @Test
    @DisplayName("När GetById körs så kastas ResourceNotFound Exception om kortet med angivna id inte hittas")
    void getByIdShouldReturn404WhenServiceThrowsResourceNotFound() throws Exception {
        Integer id = 5;
        doThrow(new ResourceNotFoundException("Could not find credit card with id: " + id))
                .when(creditCardService).getCreditCardById(id);

        mockMvc.perform(get("/api/creditcard/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/creditcard/" + id));

        verify(creditCardService).getCreditCardById(id);
    }

    @Test
    @DisplayName("PUT ska returnera 404 när metoden kastar ResourceNotFound exception")
    void updateShouldReturn404WhenServiceThrowsResourceNotFound() throws Exception {
        Integer id = 6;
        doThrow(new ResourceNotFoundException("Credit card with id " + id + " not found"))
                .when(creditCardService).updateCreditCard(eq(id), any());

        mockMvc.perform(put("/api/creditcard/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestUtils.readJson("json/creditcard-valid.json")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/creditcard/" + id));

        verify(creditCardService).updateCreditCard(eq(id), any());
    }

    @Test
    @DisplayName("Delete ska returnera 404 när servicen kastar ResourceNotFound exception")
    void deleteShouldReturn404WhenServiceThrowsResourceNotFound() throws Exception {
        Integer id = 7;
        doThrow(new ResourceNotFoundException("Credit card with id: " + id + " not found"))
                .when(creditCardService).deleteCreditCard(id);

        mockMvc.perform(delete("/api/creditcard/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/creditcard/" + id));

        verify(creditCardService).deleteCreditCard(id);
    }


    @Test
    @DisplayName("GetById ska returnera statuskod 500 när servicen kastar RuntimeException")
    void getByIdShouldReturn500WhenServiceThrowsUnexpectedException() throws Exception {
        Integer id = 8;
        doThrow(new RuntimeException("Krasch"))
                .when(creditCardService).getCreditCardById(id);

        mockMvc.perform(get("/api/creditcard/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."))
                .andExpect(jsonPath("$.path").value("/api/creditcard/" + id));

        verify(creditCardService).getCreditCardById(id);
    }
}
